package restaurantManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Customer extends JFrame {

    private static final long serialVersionUID = 1L;

    protected JButton pickupButton;
    protected JButton deliveryButton;
    protected JButton paymentButton;
    protected JPanel optionsPanel;
    private JPanel menuPanel;
    private JPanel cartPanel;
    private Menu menu;
    protected List<MenuItem> cart;
    private boolean deliverOrder;
    private Inventory inventory;
    private static final String FILE_PATH = "RestaurantOrders.json";


    public Customer() {
    	
        setTitle("Customer Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Set to full screen
        setLocationRelativeTo(null); // Center the window on the screen

        // Create menu and inventory
        menu = new Menu();
        inventory = new Inventory();
        loadInventoryFromFile();

        optionsPanel = new JPanel(new GridBagLayout());
        pickupButton = new JButton("Order Pickup");
        deliveryButton = new JButton("Order Delivery");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(100, 100, 100, 100);

        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(pickupButton, gbc);

        gbc.gridx = 1;
        optionsPanel.add(deliveryButton, gbc);

        menuPanel = new JPanel();
        cartPanel = new JPanel(new BorderLayout());
        paymentButton = new JButton("Proceed to Payment");

        cart = new ArrayList<>();
        deliverOrder = false;

        pickupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deliverOrder = false;
                optionsPanel.setVisible(false); // Hide the options panel
                displayMenu(); // Call displayMenu only when pickup is selected
            }
        });

        deliveryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deliverOrder = true;
                optionsPanel.setVisible(false); // Hide the options panel
                displayMenu(); // Call displayMenu only when delivery is selected
            }
        });

        paymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                promptForCustomerDetailsAndPlaceOrder();
            }
        });

        // Add components to the frame
        add(optionsPanel, BorderLayout.NORTH); // Change CENTER to NORTH
        add(menuPanel, BorderLayout.CENTER);
        add(cartPanel, BorderLayout.SOUTH);

        // Save inventory to file on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveInventoryToFile();
        }));
    }

    private void displayMenu() {
        menuPanel.removeAll();
        menuPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        for (MenuItem item : menu.getItems()) {
            JButton itemButton = new JButton(item.getName() + " - $" + item.getPrice());
            itemButton.setPreferredSize(new Dimension(200, 50));
            itemButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addToCart(item);
                }
            });
            menuPanel.add(itemButton, gbc);
            gbc.gridy++;
        }

        gbc.insets = new Insets(30, 10, 10, 10); // Add some space above the button
        paymentButton.setPreferredSize(new Dimension(200, 50));
        paymentButton.setFont(new Font("Arial", Font.BOLD, 16));
        paymentButton.setBackground(Color.GREEN);
        menuPanel.add(paymentButton, gbc);

        revalidate();
        repaint();
    }

    protected void addToCart(MenuItem item) {
        String quantityStr = JOptionPane.showInputDialog(null, "Enter quantity:");
        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number greater than 0.");
            return;
        }

        // Create MenuItem with quantity and add to cart
        MenuItem itemWithQuantity = new MenuItem(item.getName(), item.getDescription(), item.getPrice(), inventory.getIngredients(), quantity);
        cart.add(itemWithQuantity);

        // Deduct ingredients from inventory
        Map<String, Double> ingredients = inventory.getIngredients();
        if (!inventory.useIngredients(ingredients)) {
            JOptionPane.showMessageDialog(null, "Not enough ingredients in stock for " + item.getName());
            cart.remove(itemWithQuantity);  // Remove the item from cart if ingredients are not enough
            return;
        }

        displayCart();
    }


    protected void displayCart() {
        cartPanel.removeAll();
        JTextArea cartArea = new JTextArea();
        cartArea.setEditable(false);
        StringBuilder cartDetails = new StringBuilder();
        double subtotal = 0.0;

        for (MenuItem item : cart) {
            cartDetails.append(item.getName()).append(" - ").append(item.getQuantity()).append(" x $").append(item.getPrice()).append("\n");
            subtotal += item.getPrice() * item.getQuantity();
        }

        double tax = subtotal * 0.1; // Assuming a 10% tax rate
        double total = subtotal + tax;

        cartDetails.append("\nSubtotal: $").append(String.format("%.2f", subtotal));
        cartDetails.append("\nTax: $").append(String.format("%.2f", tax));
        cartDetails.append("\nTotal: $").append(String.format("%.2f", total));

        cartArea.setText(cartDetails.toString());
        cartPanel.add(new JScrollPane(cartArea), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void promptForCustomerDetailsAndPlaceOrder() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Your cart is empty. Please add items to cart.");
            return;
        }

        JTextField nameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField cardNumberField = new JTextField();
        JTextField expiryDateField = new JTextField();
        JTextField nameOnCardField = new JTextField();
        JTextField cvvField = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Phone Number:", phoneField,
                "Card Number:", cardNumberField,
                "Expiry Date (MM/YY):", expiryDateField,
                "Name on Card:", nameOnCardField,
                "CVV:", cvvField
        };

        int result = JOptionPane.showConfirmDialog(null, fields, "Customer Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String customerName = nameField.getText();
            String customerNumber = phoneField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String nameOnCard = nameOnCardField.getText();
            String cvv = cvvField.getText();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());

            if (customerName.isEmpty() || customerNumber.isEmpty() || cardNumber.isEmpty() ||
                    expiryDate.isEmpty() || nameOnCard.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields.");
                return;
            }

            // Calculate subtotal, tax, and total
            double subtotal = 0.0;
            for (MenuItem item : cart) {
                subtotal += item.getPrice() * item.getQuantity();
            }
            double tax = subtotal * 0.1; // Assuming a 10% tax rate
            double total = subtotal + tax;

            // Show order summary for confirmation
            StringBuilder paymentDetails = new StringBuilder("<html><h2>Order Summary</h2>");
            paymentDetails.append("Name: ").append(customerName).append("<br>");
            paymentDetails.append("Phone Number: ").append(customerNumber).append("<br>");
            paymentDetails.append("Order Type: ").append(deliverOrder ? "Delivery" : "Pickup").append("<br>");
            paymentDetails.append("Order Date: ").append(timestamp).append("<br>");
            paymentDetails.append("<h3>Items:</h3><ul>");
            for (MenuItem item : cart) {
                paymentDetails.append("<li>").append(item.getName()).append(" (x").append(item.getQuantity()).append(") - $").append(item.getPrice()).append("</li>");
            }
            paymentDetails.append("</ul>");
            paymentDetails.append("Subtotal: $").append(String.format("%.2f", subtotal)).append("<br>");
            paymentDetails.append("Tax: $").append(String.format("%.2f", tax)).append("<br>");
            paymentDetails.append("Total: $").append(String.format("%.2f", total)).append("<br>");
            paymentDetails.append("<h3>Payment Details</h3>");
            paymentDetails.append("Card Number: ").append(cardNumber).append("<br>");
            paymentDetails.append("Expiry Date: ").append(expiryDate).append("<br>");
            paymentDetails.append("Name on Card: ").append(nameOnCard).append("<br>");
            paymentDetails.append("CVV: ").append(cvv).append("</html>");

            int confirmResult = JOptionPane.showConfirmDialog(null, paymentDetails.toString(), "Confirm Payment", JOptionPane.OK_CANCEL_OPTION);
            if (confirmResult == JOptionPane.OK_OPTION) {
                // Save order to file
                saveOrderToFile(customerName, customerNumber, timestamp, cart, subtotal, tax, total, deliverOrder);

                // Clear cart after order is placed
                cart.clear();
                displayCart();
                JOptionPane.showMessageDialog(null, "Payment successful. Your order has been placed!");
            }
        }
    }


    private void loadInventoryFromFile() {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader("inventory.json")) {
            Object obj = jsonParser.parse(reader);
            JSONObject inventoryJSON = (JSONObject) obj;

            for (Object key : inventoryJSON.keySet()) {
                String ingredientName = (String) key;
                Double quantityLong = (Double) inventoryJSON.get(ingredientName);
                int quantity = quantityLong.intValue();
                inventory.addToInventory(ingredientName, quantity);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void saveInventoryToFile() {
        inventory.saveInventoryToFile();
    }

    @SuppressWarnings("unchecked")
    private void saveOrderToFile(String customerName, String customerNumber, Timestamp timestamp, List<MenuItem> cart, double subtotal, double tax, double total, boolean deliverOrder) {
        JSONArray orderList = new JSONArray();

        // Load existing orders from file
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Object obj = new JSONParser().parse(reader);
            orderList = (JSONArray) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONObject orderDetails = new JSONObject();
        orderDetails.put("customerName", customerName);
        orderDetails.put("customerNumber", customerNumber);
        orderDetails.put("timestamp", timestamp.toString());
        orderDetails.put("subtotal", subtotal);
        orderDetails.put("tax", tax);
        orderDetails.put("total", total);
        orderDetails.put("deliverOrder", deliverOrder);

        JSONArray cartArray = new JSONArray();
        for (MenuItem item : cart) {
            JSONObject cartItem = new JSONObject();
            cartItem.put("name", item.getName());
            cartItem.put("description", item.getDescription());
            cartItem.put("price", item.getPrice());
            cartItem.put("quantity", item.getQuantity());
            cartArray.add(cartItem);
        }
        orderDetails.put("cart", cartArray);

        orderList.add(orderDetails);

        // Write orders to file
        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(orderList.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Customer().setVisible(true);
            }
        });
    }
}
