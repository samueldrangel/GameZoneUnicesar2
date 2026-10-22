package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Console;
import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Service managing sales operations, validation rules, promotion applications,
 * stock updates, and warranties.
 */
public class SaleService {

    private static final String FILE_PATH = "data/sales.csv";
    private final SaleRepository repository;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final PromotionService promotionService;
    private final WarrantyService warrantyService; // Servicio de garantías integrado
    private final List<Sale> sales;

    public SaleService(SaleRepository repository, ProductService productService,
            AccessoryService accessoryService, PersonService personService,
            PromotionService promotionService, WarrantyService warrantyService) {
        this.repository = repository;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.promotionService = promotionService;
        this.warrantyService = warrantyService;

        // Combinar productos y accesorios para pasárselos a SaleRepository.loadAll
        List<Product> allCatalog = new ArrayList<>(productService.listAllProducts());
        allCatalog.addAll(accessoryService.listAllAccessories());

        List<Person> allPersons = personService.listAllPersons();

        this.sales = repository.loadAll(allPersons, allCatalog);
    }

    /**
     * Registers a sale delegating to the extended warranty version with an
     * empty list.
     */
    
    /**
     * Registers a sale with a list of purchased items, warranty assignments,
     * stock validation, inventory updates, promotion evaluation, and
     * transaction persistence.
     */
    public boolean registerSale(String saleId, Customer customer, Seller seller, List<Product> items) {
        return registerSale(saleId, customer, seller, items, new ArrayList<>());
    }

    public boolean registerSale(String saleId, Customer customer, Seller seller,
            List<Product> items, List<String> productIdsWithExtendedWarranty) {
        // 1. At least one item
        if (customer == null || seller == null || items == null || items.isEmpty()) {
            return false;
        }
        if (findSaleById(saleId) != null) {
            return false;
        }

        // 2. Resolve each item as product or accessory and validate its stock
        Map<String, Integer> requested = new LinkedHashMap<>();
        Map<String, Product> resolved = new HashMap<>();
        for (Product item : items) {
            requested.merge(item.getId(), 1, Integer::sum);
            Product current = resolveItem(item);
            if (current == null) {
                return false;
            }
            resolved.put(item.getId(), current);
        }
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            if (resolved.get(e.getKey()).getStock() < e.getValue()) {
                return false;
            }
        }

        // 3. Create the sale; the subtotal comes from the items
        Sale sale = new Sale(saleId, LocalDate.now(), customer, seller, items);

        // 4. Best promotion, computed only over the item subtotal
        Promotion best = promotionService.findBestPromotionFor(sale);
        if (best != null) {
            sale.setAppliedPromotionName(best.getName());
            sale.setDiscountAmount(best.calculateDiscount(sale));
        }

        // 5. Basic warranty per console; extended ones on request, adding their cost
        double extendedCost = 0;
        for (Product item : items) {
            if (item instanceof Console) {
                warrantyService.assignBasicWarranty(item, sale, sale.getDate());
                if (productIdsWithExtendedWarranty != null
                        && productIdsWithExtendedWarranty.contains(item.getId())) {
                    extendedCost += warrantyService
                            .assignExtendedWarranty(item, sale, sale.getDate()).getAdditionalCost();
                }
            }
        }

        // 6. Final total = subtotal - discount + extended cost (see Sale.getTotalAmount)
        sale.setExtendedWarrantyCost(extendedCost);

        // 7. Inventory update, delegated by item type
        for (Map.Entry<String, Integer> e : requested.entrySet()) {
            if (resolved.get(e.getKey()) instanceof Accessory) {
                accessoryService.updateStock(e.getKey(), -e.getValue());
            } else {
                productService.updateStock(e.getKey(), -e.getValue());
            }
        }

        // 8. Persist the sale (warranties were persisted when assigned)
        sales.add(sale);
        repository.saveAll(sales);
        return true;
    }

    private Product resolveItem(Product item) {
        return (item instanceof Accessory)
                ? accessoryService.findById(item.getId())
                : productService.findById(item.getId());
    }

    /**
     * @return a copy of all registered sales
     */
    public List<Sale> listAllSales() {
        return new ArrayList<>(sales);
    }

    /**
     * Finds a sale by its unique identifier.
     *
     * @param saleId the unique identifier of the sale
     * @return the Sale object if found, or null if it does not exist
     */
    public Sale findSaleById(String saleId) {
        if (saleId == null || saleId.isBlank()) {
            return null;
        }

        // Primero buscamos de forma eficiente en la lista en memoria ya cargada en el servicio
        for (Sale sale : sales) {
            if (sale.getSaleId().equals(saleId)) {
                return sale;
            }
        }

        // Si por alguna razón no está en memoria, reconstruimos el catálogo actual y consultamos de forma segura
        List<Product> allCatalog = new ArrayList<>(productService.listAllProducts());
        allCatalog.addAll(accessoryService.listAllAccessories());
        List<Person> allPersons = personService.listAllPersons();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;
        }

        Map<String, Person> personsById = new HashMap<>();
        for (Person p : allPersons) {
            personsById.put(p.getId(), p);
        }

        Map<String, Product> productsById = new HashMap<>();
        for (Product p : allCatalog) {
            productsById.put(p.getId(), p);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", -1);
                if (parts.length < 6) {
                    continue;
                }

                if (!parts[0].equals(saleId)) {
                    continue;
                }

                try {
                    Person customer = personsById.get(parts[2]);
                    Person seller = personsById.get(parts[3]);
                    if (!(customer instanceof Customer) || !(seller instanceof Seller)) {
                        return null;
                    }

                    List<Product> items = new ArrayList<>();
                    boolean complete = true;
                    for (String id : parts[4].split(";")) {
                        Product item = productsById.get(id);
                        if (item == null) {
                            complete = false;
                            break;
                        }
                        items.add(item);
                    }
                    if (!complete || items.isEmpty()) {
                        return null;
                    }

                    Sale sale = new Sale(parts[0], LocalDate.parse(parts[1]),
                            (Customer) customer, (Seller) seller, items);

                    if (parts.length >= 8 && !parts[6].isEmpty()) {
                        sale.setAppliedPromotionName(parts[6]);
                        sale.setDiscountAmount(Double.parseDouble(parts[7]));
                    }

                    if (parts.length >= 9 && !parts[8].isEmpty()) {
                        sale.setExtendedWarrantyCost(Double.parseDouble(parts[8]));
                    }

                    return sale;
                } catch (RuntimeException e) {
                    System.err.println("Skipping invalid sale line: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading sale: " + e.getMessage());
        }

        return null;
    }
}
