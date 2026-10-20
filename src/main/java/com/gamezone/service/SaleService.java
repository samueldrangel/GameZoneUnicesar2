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
 * Service managing sales operations, validation rules, promotion applications, and stock updates.
 */
public class SaleService {
    private static final String FILE_PATH = "sales.csv";
    private final SaleRepository repository;
    private final ProductService productService;
    private final AccessoryService accessoryService;
    private final PersonService personService;
    private final PromotionService promotionService;
    private final List<Sale> sales;

    public SaleService(SaleRepository repository, ProductService productService,
                       AccessoryService accessoryService, PersonService personService,
                       PromotionService promotionService) {
        this.repository = repository;
        this.productService = productService;
        this.accessoryService = accessoryService;
        this.personService = personService;
        this.promotionService = promotionService;

        // Combinar productos y accesorios para pasárselos a SaleRepository.loadAll
        List<Product> allCatalog = new ArrayList<>(productService.listAllProducts());
        allCatalog.addAll(accessoryService.listAllAccessories());

        List<Person> allPersons = personService.listAllPersons();

        this.sales = repository.loadAll(allPersons, allCatalog);
    }

    /**
     * Registers a sale with a list of purchased items, validates stock availability,
     * updates inventory, evaluates and applies the best promotion, and persists the transaction.
     *
     * @param id             unique sale identifier
     * @param customer       the buyer
     * @param seller         the seller
     * @param purchasedItems list of products/accessories bought
     * @return true if registered successfully, false otherwise
     */
    public boolean registerSale(String id, Customer customer, Seller seller, List<Product> purchasedItems) {
        if (id == null || id.isBlank() || customer == null || seller == null 
                || purchasedItems == null || purchasedItems.isEmpty()) {
            return false;
        }

        // 1. Validar disponibilidad de stock para cada ítem
        for (Product item : purchasedItems) {
            if (item == null || item.getStock() <= 0) {
                return false;
            }
        }

        // 2. Descontar inventario dinámicamente según el tipo de producto
        for (Product item : purchasedItems) {
            if (item instanceof Accessory) {
                accessoryService.updateStock(item.getId(), 1);
            } else {
                productService.updateStock(item.getId(), 1);
            }
        }

        // 3. Crear la transacción de venta
        Sale sale = new Sale(id, LocalDate.now(), customer, seller, purchasedItems);

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

        // 5. Agregar a la lista local y guardar en el repositorio
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
     * @param id the unique identifier of the sale
     * @return the Sale object if found, or null if it does not exist
     */
    /**
     * Finds a sale by its unique identifier.
     * 
     * @param id the unique identifier of the sale
     * @return the Sale object if found, or null if it does not exist
     */
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