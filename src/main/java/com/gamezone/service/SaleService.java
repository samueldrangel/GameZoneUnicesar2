package com.gamezone.service;

import com.gamezone.model.Accessory;
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
import java.util.List;
import java.util.Map;

/**
 * Service managing sales operations, validation rules, promotion applications, stock updates, and warranties.
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
     * Registers a sale delegating to the extended warranty version with an empty list.
     */
    public boolean registerSale(String saleId, Customer customer, Seller seller, List<Product> items) {
        return registerSale(saleId, customer, seller, items, new ArrayList<>());
    }

    /**
     * Registers a sale with a list of purchased items, warranty assignments, stock validation,
     * inventory updates, promotion evaluation, and transaction persistence.
     */
    public boolean registerSale(String saleId, Customer customer, Seller seller,
            List<Product> items, List<String> productIdsWithExtendedWarranty) {
        if (saleId == null || saleId.isBlank() || customer == null || seller == null 
                || items == null || items.isEmpty()) {
            return false;
        }

        // 1. Validar disponibilidad de stock para cada ítem
        for (Product item : items) {
            if (item == null || item.getStock() <= 0) {
                return false;
            }
        }

        // 2. Crear la transacción de venta preliminar para asociarla a las garantías
        Sale sale = new Sale(saleId, LocalDate.now(), customer, seller, items);

        // 3. Procesar garantías para consolas y calcular costo de garantía extendida
        double extendedCost = 0;
        for (Product item : items) {
            if (item instanceof com.gamezone.model.Console) {
                if (warrantyService != null) {
                    warrantyService.assignBasicWarranty(item, sale, sale.getDate());
                    if (productIdsWithExtendedWarranty != null
                            && productIdsWithExtendedWarranty.contains(item.getId())) {
                        extendedCost += warrantyService
                                .assignExtendedWarranty(item, sale, sale.getDate()).getAdditionalCost();
                    }
                }
            }
        }
        sale.setExtendedWarrantyCost(extendedCost);

        // 4. Evaluar y aplicar la mejor promoción si PromotionService está disponible
        if (promotionService != null) {
            Promotion bestPromo = promotionService.findBestPromotionFor(sale);
            if (bestPromo != null) {
                double discount = bestPromo.calculateDiscount(sale);
                if (discount > 0) {
                    sale.setAppliedPromotionName(bestPromo.getName());
                    sale.setDiscountAmount(discount);
                }
            }
        }

        // 5. Descontar inventario dinámicamente según el tipo de producto
        for (Product item : items) {
            if (item instanceof Accessory) {
                accessoryService.updateStock(item.getId(), 1);
            } else {
                productService.updateStock(item.getId(), 1);
            }
        }

        // 6. Agregar a la lista local y guardar en el repositorio
        sales.add(sale);
        repository.saveAll(sales);

        return true;
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
        if (!file.exists()) return null;

        Map<String, Person> personsById = new HashMap<>();
        for (Person p : allPersons) personsById.put(p.getId(), p);
        
        Map<String, Product> productsById = new HashMap<>();
        for (Product p : allCatalog) productsById.put(p.getId(), p);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 6) continue;

                if (!parts[0].equals(saleId)) continue;

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
                        if (item == null) { complete = false; break; }
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