package restaurantManagementSystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurantManagementSystem.OrderList.Order;

public class TestRestaurantManager {

	public static void main(String[] args) {

		testInventory();
		
		testMenu();
		
		testOrderList();

		testMenuItem();
		
		testCustomerOptionsPanelCreation();
		
		testCustomerCart();
		
		testWaiterLogin();
		
		testOrderSearch();
		
		testOrderStatusUpdate();
		
	}

	public static void testInventory() {
		//Testing initialization of inventory
		Inventory inv = new Inventory();

		//Testing view inventory
		Map<String, Double> stockItems = inv.getStockItems();

		for (String stockItem : stockItems.keySet()) {
			System.out.println(stockItem + ", " + stockItems.get(stockItem));
		}

		//Take from inventory
		inv.takeFromInventory("Onions", 15.0);

		for (String stockItem : stockItems.keySet()) {
			System.out.println(stockItem + ", " + stockItems.get(stockItem));
		}


		//Add from inventory
		inv.addToInventory("Buns", 100.0);

		for (String stockItem : stockItems.keySet()) {
			System.out.println(stockItem + ", " + stockItems.get(stockItem));
		}

		inv.saveInventory();
	}
	
	public static void testMenu() {
	    // Create an instance of the Menu
	    Menu menu = new Menu();

	    // Get the list of menu items
	    List<MenuItem> menuItems = menu.getItems();

	    // Display the menu items
	    System.out.println("Menu Items:");
	    for (MenuItem item : menuItems) {
	        System.out.println("Name: " + item.getName());
	        System.out.println("Description: " + item.getDescription());
	        System.out.println("Price: $" + item.getPrice());
	        System.out.println("Recipe:");
	        for (Map.Entry<String, Double> ingredient : item.getRecipe().entrySet()) {
	            System.out.println("- " + ingredient.getKey() + ": " + ingredient.getValue() + " units");
	        }
	        System.out.println();
	    }

	    // Add a new item to the menu and test if it's added successfully
	    MenuItem newMenuItem = new MenuItem("New Item", "Description of the new item", 12.99, new HashMap<>(), 0);
	    menu.addItem(newMenuItem);

	    // Display the menu items again to verify the addition
	    System.out.println("Updated Menu Items:");
	    for (MenuItem item : menu.getItems()) {
	        System.out.println("Name: " + item.getName());
	        System.out.println("Description: " + item.getDescription());
	        System.out.println("Price: $" + item.getPrice());
	        System.out.println("Recipe:");
	        for (Map.Entry<String, Double> ingredient : item.getRecipe().entrySet()) {
	            System.out.println("- " + ingredient.getKey() + ": " + ingredient.getValue() + " units");
	        }
	        System.out.println();
	    }
	}
	
	public static void testOrderList() {
        // Create some sample menu items for testing
        MenuItem burger = new MenuItem("Classic Burger", "Juicy beef patty with lettuce and tomato", 8.99, new HashMap<>(), 0);
        MenuItem pizza = new MenuItem("Margherita Pizza", "Traditional pizza topped with tomatoes and mozzarella", 10.99, new HashMap<>(), 0);

        // Create a sample order
        List<MenuItem> items = new ArrayList<>();
        items.add(burger);
        items.add(pizza);
        OrderList.Order order = new OrderList.Order(items, true, "Credit Card", "John Doe", "1234567890");

        // Add the order to the order list
        OrderList.addOrder(order);

        // Retrieve orders from the order list
        List<OrderList.Order> orders = OrderList.getOrders();

        // Display the orders
        for (OrderList.Order o : orders) {
            System.out.println("Order ID: " + o.getId());
            System.out.println("Items:");
            for (MenuItem item : o.getItems()) {
                System.out.println("- " + item.getName() + ": $" + item.getPrice());
            }
            System.out.println("Delivery: " + (o.isDelivery() ? "Yes" : "No"));
            System.out.println("Payment Method: " + o.getPaymentMethod());
            System.out.println("Customer Name: " + o.getCustomerName());
            System.out.println("Customer Number: " + o.getCustomerNumber());
            System.out.println("Status: " + o.getStatus());
            System.out.println("Order Date and Time: " + o.getOrderDateTime());
        }
    }
	
	public static void testMenuItem() {
	    // Create a sample recipe
	    Map<String, Double> recipe = new HashMap<>();
	    recipe.put("Tomato", 2.0);
	    recipe.put("Lettuce", 1.0);
	    recipe.put("Beef Patty", 1.0);

	    // Create a MenuItem instance
	    MenuItem menuItem = new MenuItem("Classic Burger", "Juicy beef patty with lettuce and tomato", 8.99, recipe, 1);

	    // Display the details of the MenuItem
	    System.out.println("MenuItem Details:");
	    System.out.println("Name: " + menuItem.getName());
	    System.out.println("Description: " + menuItem.getDescription());
	    System.out.println("Price: $" + menuItem.getPrice());
	    System.out.println("Recipe:");
	    for (Map.Entry<String, Double> ingredient : menuItem.getRecipe().entrySet()) {
	        System.out.println("- " + ingredient.getKey() + ": " + ingredient.getValue() + " units");
	    }
	    System.out.println("Quantity: " + menuItem.getQuantity());

	    // Update the quantity of the MenuItem and display the updated details
	    menuItem.setQuantity(2);
	    System.out.println("Updated Quantity: " + menuItem.getQuantity());

	    // Test the toString() method of MenuItem
	    System.out.println("MenuItem Details (toString()):");
	    System.out.println(menuItem.toString());
	}
	
	public static void testCustomerOptionsPanelCreation() {
        Customer customer = new Customer();

        // Ensure options panel is created with the expected buttons
        assertNotNull(customer.optionsPanel);
        assertNotNull(customer.pickupButton);
        assertNotNull(customer.deliveryButton);
        assertNotNull(customer.paymentButton);
    }

    public static void testCustomerCart() {
        Customer customer = new Customer();

        // Simulate adding items to the cart
        MenuItem item1 = new MenuItem("Classic Burger", "Juicy beef patty with lettuce and tomato", 8.99, new HashMap<>(), 1);
        MenuItem item2 = new MenuItem("Margherita Pizza", "Traditional pizza topped with tomatoes and mozzarella", 10.99, new HashMap<>(), 2);

        customer.addToCart(item1);
        customer.addToCart(item2);

        // Check if items are added to the cart and displayed correctly
        assertEquals(2, customer.cart.size());

        // Simulate clearing the cart
        customer.cart.clear();

        // Ensure the cart is empty after clearing
        assertTrue(customer.cart.isEmpty());
    }
    
    public static void testWaiterLogin() {
        Waiter waiter = new Waiter();

        // Simulate valid login
        assertTrue(waiter.isValidLogin("WAITER", "server123"));

        // Simulate invalid login
        assertFalse(waiter.isValidLogin("wrong_username", "wrong_password"));
    }

    public static void testOrderSearch() {
        Waiter waiter = new Waiter();

        // Simulate searching for an existing order
        Order order = waiter.findOrder("John Doe");
        assertNotNull(order);

        // Simulate searching for a non-existing order
        Order nonExistingOrder = waiter.findOrder("Non Existing Customer");
        assertNull(nonExistingOrder);
    }

    public static void testOrderStatusUpdate() {
        Waiter waiter = new Waiter();
        List<Order> orders = waiter.loadOrdersFromFile();

        // Simulate updating order status
        Order order = orders.get(0);
        Order.OrderStatus originalStatus = order.getStatus();
        order.setStatus(Order.OrderStatus.DELIVERED);
        waiter.updateOrderStatusInFile(order);

        // Reload orders and check if status is updated
        List<Order> updatedOrders = waiter.loadOrdersFromFile();
        Order updatedOrder = updatedOrders.stream().filter(o -> o.getId() == order.getId()).findFirst().orElse(null);
        assertNotNull(updatedOrder);
        assertEquals(Order.OrderStatus.DELIVERED, updatedOrder.getStatus());

        // Restore original status
        order.setStatus(originalStatus);
        waiter.updateOrderStatusInFile(order);
    }
}
    
