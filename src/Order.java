import java.util.HashSet;
import java.util.Set;

public class Order {
    private String orderId;
    private String userId;
    private String proId;
    private String orderTime;
    private static final Set<String> existingOrderIds = new HashSet<>();
    public Order(String orderId,String userId, String proId, String orderTime) {
        if(orderId == null || !orderId.matches("^o_\\d{5}$"))
            throw new IllegalArgumentException("orderID cannot be null and must follow the format\"o_(5digits)\"");
        if(existingOrderIds.contains(orderId))
            throw new IllegalArgumentException("orderId must be unique. Duplicate found: " + orderId);
        this.orderId=orderId;
        existingOrderIds.add(orderId);
        this.userId=userId;
        this.proId=proId;
        if (orderTime == null || !orderTime.matches("^([0-2][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}_([01][0-9]|2[0-3]):([0-5][0-9]:([0-5][0-9])$"))
            throw new IllegalArgumentException("Invalid order time format. Must be DD-MM-YYYY_HH:MM:SS");
        this.orderTime=orderTime;
    }
    public Order() {
        // String generatedId;
        // do {
        //     int randomNum = (int) (Math.random() * 100000);
        //     generatedId = String.format("o_%05d", randomNum);
        // } while (existingOrderIds.contains(generatedId));
        // this.orderId = generatedId;
        // existingOrderIds.add(generatedId);

        this.orderId="o_12345";
        this.userId = "u_0000000000";
        this.proId = "p_00000";
        this.orderTime = "01-01-2025_00:00:00";
    }
    public String toString(){
        return orderId;
    }
    

}
