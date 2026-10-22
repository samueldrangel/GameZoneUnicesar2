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
import com.gamezone.model.*;
import java.util.List;

/** Validation rules for sales. */
public final class SaleValidator {
    private SaleValidator() { }

    /**
     * @throws BusinessRuleException if the customer, the seller or the items are missing
     * @throws InvalidDataException  if the item list is null
     */
    public static void validateSaleData(Customer customer, Seller seller, List<Product> products) {
        if (customer == null) throw new BusinessRuleException("La venta debe tener un cliente");
        if (seller == null) throw new BusinessRuleException("La venta debe tener un vendedor");
        if (products == null) throw new InvalidDataException("productos", "la lista no puede ser nula");
        if (products.isEmpty()) throw new BusinessRuleException("La venta debe tener al menos un producto");
    }

    /** @throws ResourceNotFoundException if the sale is null */
    public static void validateSaleExists(Sale sale, String saleId) {
        if (sale == null) throw new ResourceNotFoundException("La venta", saleId);
    }
}