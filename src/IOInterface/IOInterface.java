package IOInterface;

import java.util.List;
import java.util.Scanner;

import Model.Order;
import Model.Product;
import Model.User;

public class IOInterface {
    private Scanner scanner = new Scanner(System.in);
    private static IOInterface instance;
    private IOInterface(){}
    public static IOInterface getInstance(){
        if(instance==null){
            instance = new IOInterface();
        }
        return instance;
    }
    public String[] getUserInput(String message, int numOfArgs) {
        System.out.println(message);
        String line;
        String[] result;
        do{
            line = scanner.nextLine().trim();
            String[] temp = line.split("\\s+"); 
            // \\s+ là space (bao nhiêu space cũng được)
            result = new String[numOfArgs];
            for(int i =0;i<numOfArgs;i++){
                if(i<temp.length) result[i]=temp[i];
                else result[i]=null;
            }
        }while(line.isBlank());
        return result;
    }
    public void mainMenu(){
        System.out.println("(1) Login");
        System.out.println("(2) Register");
        System.out.println("(3) Quit");
    }
    public void adminMenu(){
        System.out.println("(1) Show products");
        System.out.println("(2) Add customers");
        System.out.println("(3) Show customers");
        System.out.println("(4) Show orders");
        System.out.println("(5) Generate test data");
        System.out.println("(6) Generate all statiscal figure");
        System.out.println("(7) Delete all data");
        System.out.println("(8) Logout");
    
    }
    public void customerMenu(){
        System.out.println("(1) Show profile");
        System.out.println("(2) Update profile");
        System.out.println("(3) Show products (user input could be \"3 keyword\" or \"3\")");
        System.out.println("(4) Show history orders");
        System.out.println("(5) Generate all consumption figure");
        System.out.println("(6) Logout");
    }
    public void showList(String userRole, String listType, List<?> objectList,int pageNumber, int totalPages) {
    int i=0;
    for (Object obj : objectList) {
        i++;
        if ("Customer".equalsIgnoreCase(listType) && obj instanceof User&&userRole.equals("admin")) {
            System.out.println("("+i+") "+((User) obj).toString());
        } else if ("Product".equalsIgnoreCase(listType) && obj instanceof Product) {
            System.out.println("("+i+") "+((Product) obj).toString());
        } else if ("Order".equalsIgnoreCase(listType) && obj instanceof Order) {
            System.out.println("("+i+") "+((Order) obj).toString());
        } else {
            System.out.println("Unknown object type.");
        }
    }
    i=0;
    System.out.println(pageNumber+"/"+totalPages);    
    }
    public void printErrorMessage(String errorSource, String errorMessage) {
        System.out.println("XXX ERROR in [" + errorSource + "]: " + errorMessage);
    }
    public void printMessage(String message){
        System.out.println(message);
    }
    public void printObject(Object targetObject) {
        System.out.println(targetObject.toString());
    }
}
