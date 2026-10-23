package com.gamezone.ui;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int readInt(String prompt) {
        System.out.print(prompt);
        int value = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer
        return value;
    }

    public double readDouble(String prompt) {
        System.out.print(prompt);
        double value = scanner.nextDouble();
        scanner.nextLine(); // limpiar buffer
        return value;
    }

    public List<String> readList(String prompt) {
        // Lógica para leer lista separada por comas o punto y coma
        String input = readLine(prompt);
        return List.of(input.split("[,;]"));
    }

    public LocalDate readDate(String prompt) {
        String input = readLine(prompt + " (YYYY-MM-DD): ");
        return LocalDate.parse(input);
    }
}


