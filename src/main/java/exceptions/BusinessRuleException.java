/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 *
 * @author Samuel
 */

/** Thrown when a business rule is violated. */
public class BusinessRuleException extends GameZoneException {
    /** @param message the violated rule, in Spanish */
    public BusinessRuleException(String message) {
        super(message, "GZ-BUSINESS");
    }
}
