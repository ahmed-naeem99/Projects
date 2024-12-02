package restaurantManagementSystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OrderList {
	
	
    public static class Order {
        private static int currentId = 1;
        private int id;
        private List<MenuItem> items;
        private boolean delivery;
        private String paymentMethod;
        private OrderStatus status;
        private String customerName; 
        private String customerNumber;
        @SuppressWarnings("unused")
		private LocalDateTime orderDateTime;

        public enum OrderStatus {
        	PLACED,
            IN_PROGRESS,
            READY,
            DELIVERED
        }

        public Order(List<MenuItem> items, boolean delivery, String paymentMethod, String name, String number) {
            this.id = getNextOrderId();
            this.items = items;
            this.delivery = delivery;
            this.paymentMethod = paymentMethod;
            this.status = OrderStatus.PLACED; // Default status is PLACED
            this.customerName = name;
            this.customerNumber = number;
            
            // Increment order ID based on the last order ID in RestaurantOrders.json
            JSONParser parser = new JSONParser();
            try {
                FileReader reader = new FileReader("RestaurantOrders.json");
                Object obj = parser.parse(reader);
                if (obj instanceof JSONArray) {
                    JSONArray ordersArray = (JSONArray) obj;
                    if (!ordersArray.isEmpty()) {
                        JSONObject lastOrder = (JSONObject) ordersArray.get(ordersArray.size() - 1);
                        Object lastOrderIdObject = lastOrder.get("id");
                        if (lastOrderIdObject != null) {
                            int lastOrderId = Integer.parseInt(lastOrderIdObject.toString());
                            this.id = lastOrderId + 1;
                        } else {

                            this.id = 1; // Example: Assigning a default starting ID of 1
                        }
                    } else {
                        // Handle case where ordersArray is empty (no previous orders)
                        this.id = 1; // Example: Assigning a default starting ID of 1
                    }
                }
            } catch (FileNotFoundException e) {
                // File not found, assume starting ID is 1
                this.id = 1;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }

        // Getter and setter for customer name
        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }
        
        // Getter and setter for customer number
        public String getCustomerNumber() {
            return customerNumber;
        }

        public void setCustomerNumber(String customerNumber) {
            this.customerNumber = customerNumber;
        }

        public OrderStatus getStatus() {
            return status;
        }
        
        public LocalDateTime getOrderDateTime() {
            return this.orderDateTime = LocalDateTime.now();
        }

        public void setStatus(OrderStatus status) {
            this.status = status;
        }
        
        private synchronized int getNextOrderId() {
            return currentId++;
        }

        public int getId() {
            return id;
        }

        public List<MenuItem> getItems() {
            return items;
        }

        public boolean isDelivery() {
            return delivery;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }
    }

    private static List<Order> orders = new ArrayList<>();

    public static void addOrder(Order order) {
        orders.add(order);
    }

    public static List<Order> getOrders() {
        return orders;
    }
}