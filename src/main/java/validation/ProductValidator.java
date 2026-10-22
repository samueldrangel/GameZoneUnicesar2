/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package validation;

/**
 *
 * @author Samuel
 */

import exceptions.*;
import com.gamezone.model.Product;

/** Validation rules for products. */
public final class ProductValidator {
    private ProductValidator() { }

    /** @throws InvalidDataException if a value is not valid */
    public static void validateProductData(String id, String title, double price, int stock) {
        if (id == null || id.isBlank()) throw new InvalidDataException("identificador", "no puede estar vacío");
        if (title == null || title.isBlank()) throw new InvalidDataException("título", "no puede estar vacío");
        if (price <= 0) throw new InvalidDataException("precio", "debe ser positivo");
        if (stock < 0) throw new InvalidDataException("stock", "no puede ser negativo");
    }

    /** @throws BusinessRuleException if the stock is not enough */
    public static void validateStockAvailability(Product product, int requestedQuantity) {
        if (product.getStock() < requestedQuantity) {
            throw new BusinessRuleException("Stock insuficiente para '" + product.getTitle()
                    + "': disponible " + product.getStock() + ", solicitado " + requestedQuantity);
        }
    }

    /** @throws ResourceNotFoundException if the product is null */
    public static void validateProductExists(Product product, String productId) {
        if (product == null) throw new ResourceNotFoundException("El producto", productId);
    }
}
