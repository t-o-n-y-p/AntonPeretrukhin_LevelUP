package ru.levelp.at.hw2.task1;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Операция над двумя числами");
        System.out.print("Введите первое число: ");
        String input = scanner.next();
        if (!input.matches("[-]?[0-9]+(\\.[0-9]+)?")) {
            System.out.println("Ошибка ввода: введено не число");
            return;
        }
        BigDecimal arg1 = new BigDecimal(input);
        String operation;
        List<String> validOperations = List.of("1", "2", "3", "4");
        do {
            System.out.println("Введите операцию:");
            System.out.println("    1. Прибавить");
            System.out.println("    2. Вычесть");
            System.out.println("    3. Умножить");
            System.out.println("    4. Разделить");
            System.out.print("> ");
            operation = scanner.next();
        } while (!validOperations.contains(operation));
        System.out.print("Введите второе число: ");
        input = scanner.next();
        if (!input.matches("[-]?[0-9]+(\\.[0-9]+)?")) {
            System.out.println("Ошибка ввода: введено не число");
            return;
        }
        BigDecimal arg2 = new BigDecimal(input);
        switch (operation) {
            case "1":
                System.out.println("Результат = " + arg1.add(arg2));
                break;
            case "2":
                System.out.println("Результат = " + arg1.subtract(arg2));
                break;
            case "3":
                System.out.println("Результат = " + arg1.multiply(arg2));
                break;
            case "4":
                if (arg2.equals(BigDecimal.ZERO)) {
                    System.out.println("Ошибка: деление на ноль");
                } else {
                    System.out.println("Результат = " + arg1.divide(arg2, MathContext.DECIMAL32));
                }
                break;
            default:
                System.out.println("Неверная операция");
        }
    }

}
