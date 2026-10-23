package com.gamezone.ui;

import com.gamezone.service.AccessoryService;
import java.util.ArrayList;
import java.util.List;

public class AccessoryMenu {
    private final AccessoryService accessoryService;
    private final ConsoleInput input;

    public AccessoryMenu(AccessoryService accessoryService, ConsoleInput input) {
        this.accessoryService = accessoryService;
        this.input = input;
    }

    public void registerController() {
        String id = input.readLine("Ingrese ID: ");
        String title = input.readLine("Ingrese Título: ");
        double price = input.readDouble("Ingrese Precio: ");
        int stock = input.readInt("Ingrese Stock: ");
        String connType = input.readLine("Ingrese Tipo de Conexión (Wireless/Wired): ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerController(id, title, price, stock, compatibleConsoles, connType);
        System.out.println("¡Control registrado exitosamente!");
    }

    public void registerCable() {
        String id = input.readLine("Ingrese ID: ");
        String title = input.readLine("Ingrese Título: ");
        double price = input.readDouble("Ingrese Precio: ");
        int stock = input.readInt("Ingrese Stock: ");
        double length = input.readDouble("Ingrese Longitud (Metros): ");
        String connector = input.readLine("Ingrese Tipo de Conector: ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerCable(id, title, price, stock, compatibleConsoles, length, connector);
        System.out.println("¡Cable registrado exitosamente!");
    }

    public void registerMemory() {
        String id = input.readLine("Ingrese ID: ");
        String title = input.readLine("Ingrese Título: ");
        double price = input.readDouble("Ingrese Precio: ");
        int stock = input.readInt("Ingrese Stock: ");
        int capacity = input.readInt("Ingrese Capacidad (GB): ");
        String memoryType = input.readLine("Ingrese Tipo de Memoria: ");

        List<String> compatibleConsoles = promptCompatibleConsoles();

        accessoryService.registerMemory(id, title, price, stock, compatibleConsoles, capacity, memoryType);
        System.out.println("¡Memoria registrada exitosamente!");
    }

    private List<String> promptCompatibleConsoles() {
        List<String> compatibleConsoles = new ArrayList<>();
        String opt = input.readLine("¿Agregar ID de consola compatible? (s/n): ");
        while (opt.equalsIgnoreCase("s") || opt.equalsIgnoreCase("y")) {
            compatibleConsoles.add(input.readLine("Ingrese ID de la consola: "));
            opt = input.readLine("¿Agregar otra consola compatible? (s/n): ");
        }
        return compatibleConsoles;
    }
}
