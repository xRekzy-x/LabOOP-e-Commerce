package operation;


import model.Product;
import model.Customer;
import model.Order;
import model.OrderListResult;
import model.ProductListResult;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class OrderOperation {
    private static OrderOperation instance;
    private final String orderFile = "data/orders.txt";

    private OrderOperation() {}


    //2.10.1
    public static OrderOperation getInstance() {
        if (instance == null) {
            instance = new OrderOperation();
        }
        return instance;
    }
    
    
    //2.10.2
    public String generateUniqueOrderId() {
        Random rand = new Random();
        return "o_" + (10000 + rand.nextInt(90000));
    }


    //2.10.3
    public boolean createAnOrder(String customerId, String productId, String createTime) {
        try {
            if (createTime == null || createTime.isEmpty()) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH:mm:ss");
                createTime = dtf.format(LocalDateTime.now());
            }

            String orderId = generateUniqueOrderId();
            Order order = new Order(orderId, customerId, productId, createTime);

            BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile, true));
            writer.write(order.toString());
            writer.newLine();
            writer.close();

            return true;
        } catch (IOException e) {
            System.err.println("❌ Error creating order: " + e.getMessage());
            return false;
        }
    }
    
    
    // 2.10.4
    public boolean deleteOrder(String orderId) {
        boolean found = false;
        try {
            List<String> lines = Files.readAllLines(Paths.get(orderFile));
            List<String> updated = new ArrayList<>();

            for (String line : lines) {
                if (line.contains("\"order_id\":\"" + orderId + "\"")) {
                    found = true;
                    continue;
                }
                updated.add(line);
            }

            Files.write(Paths.get(orderFile), updated);
        } catch (IOException e) {
            System.err.println("❌ Error deleting order: " + e.getMessage());
        }
        return found;
    }


    // 2.10.5
    public OrderListResult getOrderList(String customerId, int pageNumber) {
        List<Order> allOrders = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(orderFile));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("\"user_id\":\"" + customerId + "\"")) {
                    Order order = parseOrderFromLine(line);
                    if (order != null) allOrders.add(order);
                }
            }

            reader.close();
        } catch (IOException e) {
            System.err.println("❌ Error reading orders: " + e.getMessage());
        }

        int totalOrders = allOrders.size();
        int totalPages = (int) Math.ceil(totalOrders / 10.0);
        int startIndex = (pageNumber - 1) * 10;
        int endIndex = Math.min(startIndex + 10, totalOrders);

        List<Order> pageList = allOrders.subList(startIndex, endIndex);

        return new OrderListResult(pageList, pageNumber, totalPages);
    }

    // 2.10.6
    public void generateTestOrderData() {
                System.out.println("🧪 [generateTestOrderData] - Bạn cần triển khai thêm lớp khách và sản phẩm.");
    }

}
