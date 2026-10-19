package com.gamezone.service;

import com.gamezone.model.Accessory;
import com.gamezone.model.Customer;
import com.gamezone.model.Person;
import com.gamezone.model.Product;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.model.Seller;
import com.gamezone.persistence.SaleRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service managing sales operations, validation rules, promotion applications, and stock updates.
 */
public class SaleService {
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
}