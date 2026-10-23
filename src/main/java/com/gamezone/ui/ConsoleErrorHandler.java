package com.gamezone.ui;

import exceptions.*;

/** Shows differentiated messages for each kind of system exception. */
public class ConsoleErrorHandler {
    /** Runs the action and reports any GameZoneException in Spanish. */
    public void run(Runnable action) {
        try {
            action.run();
        } catch (ResourceNotFoundException e) {
            System.out.println("No se encontró el recurso solicitado: " + e.getMessage());
        } catch (BusinessRuleException e) {
            System.out.println("No se puede completar la operación: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("Los datos ingresados no son válidos: " + e.getMessage());
        } catch (PersistenceException e) {
            System.out.println("Ocurrió un problema al acceder a los datos del sistema. Contacte al administrador.");
        } catch (GameZoneException e) {
            System.out.println("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}