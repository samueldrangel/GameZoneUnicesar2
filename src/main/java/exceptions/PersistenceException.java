/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/**
 *
 * @author Samuel
 */

/** Thrown when a file cannot be read or written. */
public class PersistenceException extends GameZoneException {
    /** @param message file and operation @param cause the original exception */
    public PersistenceException(String message, Throwable cause) {
        super(message, "GZ-PERSISTENCE", cause);
    }
}