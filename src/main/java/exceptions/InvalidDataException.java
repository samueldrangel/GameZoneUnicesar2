/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 *
 * @author Samuel
 */

/** Thrown when an input value is not valid. */
public class InvalidDataException extends GameZoneException {
    /**
     * @param field   the invalid field
     * @param problem what is wrong, in Spanish
     */
    public InvalidDataException(String field, String problem) {
        super(String.format("El campo '%s' no es válido: %s", field, problem), "GZ-INVALID");
    }
}
