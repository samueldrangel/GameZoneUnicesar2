/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 *
 * @author Samuel
 */

/** Thrown when a requested resource does not exist. */
public class ResourceNotFoundException extends GameZoneException {
    /**
     * @param resourceType resource name with its article, for example "El producto"
     * @param identifier   the id that was searched
     */
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s con identificador %s no fue encontrado", resourceType, identifier),
                "GZ-404");
    }
}
