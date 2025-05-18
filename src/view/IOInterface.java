package view;

import java.util.*;
import model.*;



public class IOInterface {
    
    private static IOInterface instance;
    private Scanner scanner;

    private IOInterface() {
        scanner = new Scanner(System.in);
    }
    //2.11.1 
    public static IOInterface getInstance() {
        if (instance == null) {
            instance = new IOInterface();
        }
        return instance;
    }

    //2.11.2
    public String[] getUserInput(String message, int numOfArgs) {
        System.out.print(message);
        String line = scanner.nextLine().trim();
        String[] parts = line.split(" ");

        String[] result = new String[numOfArgs];
        for (int i = 0; i < numOfArgs; i++) {
            result[i] = (i < parts.length) ? parts[i] : "";
        }
        return result;
    }

    //2.11.3
    public void mainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("(1) Login");
        System.out.println("(2) Register");
        System.out.println("(3) Quit");
        System.out.print("Choose option: ");
    }

    //2.11.4
    public void adminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("(1) Show products");
        System.out.println("(2) Add customers");
        System.out.println("(3) Show customers");
        System.out.println("(4) Show orders");
        System.out.println("(5) Generate test data");
        System.out.println("(6) Generate all statistical figures");
        System.out.println("(7) Delete all data");
        System.out.println("(8) Logout");
        System.out.print("Choose option: ");
    }

    //2.11.5
    public void customerMenu() {
        System.out.println("\n=== Customer Menu ===");
        System.out.println("(1) Show profile");
        System.out.println("(2) Update profile");
        System.out.println("(3) Show products (or 3 keyword)");
        System.out.println("(4) Show history orders");
        System.out.println("(5) Generate all consumption figures");
        System.out.println("(6) Logout");
        System.out.print("Choose option: ");
    }

    //2.11.6
    public void showList(String userRole, String listType, List<?> objectList, int pageNumber, int totalPages) {
        System.out.println("\n--- " + listType.toUpperCase() + " LIST (Page " + pageNumber + " / " + totalPages + ") ---");
        int count = 1;
        for (Object obj : objectList) {
            System.out.println((count++) + ". " + obj.toString());
        }
        if (objectList.isEmpty()) {
            System.out.println("No data available.");
        }
    }


    //2.11.7
    public void printErrorMessage(String errorSource, String errorMessage) {
        System.out.println("[ERROR - " + errorSource + "]: " + errorMessage);
    }


    //2.11.8
    public void printMessage(String message) {
        System.out.println(message);
    }
    
    //2.11.9
    public void printObject(Object targetObject) {
        if (targetObject != null) {
            System.out.println(targetObject.toString());
        } else {
            System.out.println("[NULL object]");
        }
    }
}
