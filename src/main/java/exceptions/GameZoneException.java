/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exceptions;

/** Root of all system exceptions. Unchecked, with an error code. */
public abstract class GameZoneException extends RuntimeException {
    private final String errorCode;

    /** @param message the message */
    public GameZoneException(String message) {
        this(message, "GZ-000");
    }

    /** @param message the message @param errorCode the error code */
    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /** Keeps the original cause, used by persistence errors. */
    protected GameZoneException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /** @return the error code */
    public String getErrorCode() { return errorCode; }
}
