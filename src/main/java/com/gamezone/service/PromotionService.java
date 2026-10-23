package com.gamezone.service;

import com.gamezone.model.BulkPurchaseDiscount;
import com.gamezone.model.CategoryDiscount;
import com.gamezone.model.PercentageDiscount;
import com.gamezone.model.ProductCategory;
import com.gamezone.model.Promotion;
import com.gamezone.model.Sale;
import com.gamezone.persistence.PromotionRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Business rules for promotions, including the selection of the best promotion
 * for a sale.
 */
public class PromotionService {

    private final PromotionRepository repository;
    private final List<Promotion> promotions;

    /**
     * Creates the service and loads the stored promotions.
     *
     * @param repository the promotion repository
     */
    public PromotionService(PromotionRepository repository) {
        this.repository = repository;
        this.promotions = new ArrayList<>(repository.loadAll());
    }

    /**
     * Registers a percentage promotion.
     *
     * @return false if the id already exists
     * @throws IllegalArgumentException if a value is invalid
     */
    public boolean registerPercentageDiscount(String id, String name, LocalDate start,
            LocalDate end, double percentage) {
        validateCommon(id, name, start, end);
        validatePercentage(percentage);
        if (findById(id) != null) {
            return false;
        }
        promotions.add(new PercentageDiscount(id, name, start, end, percentage));
        repository.saveAll(promotions);
        return true;
    }

    /**
     * Registers a category promotion.
     *
     * @return false if the id already exists
     * @throws IllegalArgumentException if a value is invalid
     */
    private static final List<String> ALLOWED_CATEGORIES = List.of("VIDEOGAME", "CONSOLE", "ACCESSORY");

    public boolean registerCategoryDiscount(String id, String name, LocalDate start,
            LocalDate end, double percentage, String category) {
        validateCommon(id, name, start, end);
        validatePercentage(percentage);

        boolean valid = Arrays.stream(ProductCategory.values()).anyMatch(c -> c.name().equals(category));
        if (!valid) {
            throw new IllegalArgumentException("La categoría debe ser " + allowedCategoriesText() + ".");
        }

        if (findById(id) != null) {
            return false;
        }
        promotions.add(new CategoryDiscount(id, name, start, end, percentage, category));
        repository.saveAll(promotions);
        return true;
    }

    /**
     * Registers a bulk purchase promotion.
     *
     * @return false if the id already exists
     * @throws IllegalArgumentException if a value is invalid
     */
    public boolean registerBulkPurchaseDiscount(String id, String name, LocalDate start,
            LocalDate end, int minimumQuantity,
            double percentage) {
        validateCommon(id, name, start, end);
        validatePercentage(percentage);
        if (minimumQuantity < 1) {
            throw new IllegalArgumentException("La cantidad mínima debe ser al menos 1.");
        }
        if (findById(id) != null) {
            return false;
        }
        promotions.add(new BulkPurchaseDiscount(id, name, start, end, minimumQuantity, percentage));
        repository.saveAll(promotions);
        return true;
    }

    /**
     * @return a copy of all registered promotions
     */
    public List<Promotion> listAllPromotions() {
        return new ArrayList<>(promotions);
    }

    /**
     * @return the promotions valid on the current date
     */
    public List<Promotion> listActivePromotions() {
        return activeOn(LocalDate.now());
    }

    /**
     * Finds the valid promotion that grants the highest discount to the sale.
     *
     * @param sale the sale to evaluate
     * @return the best promotion, or null if none applies or the best discount
     * is zero
     */
    public Promotion findBestPromotionFor(Sale sale) {
        Promotion best = null;
        double bestDiscount = 0;
        for (Promotion p : activeOn(sale.getDate())) {
            double discount = p.calculateDiscount(sale);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                best = p;
            }
        }
        return best;
    }

    /**
     * Finds a promotion by id.
     *
     * @param id the promotion id
     * @return the promotion, or null if not found
     */
    public Promotion findById(String id) {
        for (Promotion p : promotions) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    private List<Promotion> activeOn(LocalDate date) {
        List<Promotion> result = new ArrayList<>();
        for (Promotion p : promotions) {
            if (p.isActive(date)) {
                result.add(p);
            }
        }
        return result;
    }

    private void validateCommon(String id, String name, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("El identificador y el nombre son obligatorios.");
        }
        if (id.contains(",") || name.contains(",")) {
            throw new IllegalArgumentException("El identificador y el nombre no pueden contener comas.");
        }
        if (start == null || end == null || end.isBefore(start)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la de inicio.");
        }
    }

    private void validatePercentage(double percentage) {
        if (percentage <= 0 || percentage > 100) {
            throw new IllegalArgumentException("El porcentaje debe estar entre 0 y 100.");
        }
    }

    /**
     * Applies the best valid promotion to the sale, if any.
     */
    public void applyBestPromotion(Sale sale) {
        Promotion best = findBestPromotionFor(sale);
        if (best != null) {
            sale.setAppliedPromotionName(best.getName());
            sale.setDiscountAmount(best.calculateDiscount(sale));
        }
    }

    private static String allowedCategoriesText() {
        ProductCategory[] v = ProductCategory.values();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) {
            if (i > 0) {
                sb.append(i == v.length - 1 ? " o " : ", ");
            }
            sb.append(v[i].name());
        }
        return sb.toString();   // VIDEOGAME, CONSOLE o ACCESSORY
    }
}
