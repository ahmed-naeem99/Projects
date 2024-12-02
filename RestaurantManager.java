package restaurantManagementSystem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RestaurantManager extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel welcomeLabel;
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public RestaurantManager() {
        setTitle("Restaurant Manager Window");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); // Set the initial size
        setLocationRelativeTo(null); // Center the window on the screen

        // Set layout and font
        setLayout(new BorderLayout());
        setFont(new Font("Serif", Font.PLAIN, 18));

        // Initialize welcome label
        welcomeLabel = new JLabel("", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.PLAIN, 24));
        add(welcomeLabel, BorderLayout.NORTH);

        // Create buttons
        JButton viewInventoryButton = createStyledButton("View Inventory");
        JButton editInventoryButton = createStyledButton("Edit Inventory");
        JButton editMenuButton = createStyledButton("Edit Menu");
        JButton viewOrdersButton = createStyledButton("View Orders");

        // Add action listeners
        viewInventoryButton.addActionListener(e -> viewInventory());
        editInventoryButton.addActionListener(e -> editInventory());
        editMenuButton.addActionListener(e -> editMenu());
        viewOrdersButton.addActionListener(e -> viewOrders());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        buttonPanel.add(viewInventoryButton);
        buttonPanel.add(editInventoryButton);
        buttonPanel.add(editMenuButton);
        buttonPanel.add(viewOrdersButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Add order table
        setupOrderTable();

        // Prompt login
        promptLogin();
    }

    private void setupOrderTable() {
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Items", "Status"}, 0);
        orderTable = new JTable(tableModel);
        orderTable.setFont(new Font("Serif", Font.PLAIN, 18));
        orderTable.setRowHeight(30);
        orderTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18));

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JButton updateStatusButton = createStyledButton("Update Status");
        updateStatusButton.addActionListener(e -> updateOrderStatus());
        tablePanel.add(updateStatusButton, BorderLayout.SOUTH);

        add(tablePanel, BorderLayout.SOUTH);
    }

    private void promptLogin() {
        while (true) {
            String username = JOptionPane.showInputDialog(this, "Enter username:");
            String password = JOptionPane.showInputDialog(this, "Enter password:");
            if (isValidLogin(username, password)) {
                welcomeLabel.setText("Welcome, " + username + "!");
                break;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password.");
            }
        }
    }

    private boolean isValidLogin(String username, String password) {
        return username.equals("MANAGER") && password.equals("manager123");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.PLAIN, 18));
        button.setBackground(new Color(199, 0, 57));
        button.setForeground(Color.BLACK);
        button.setPreferredSize(new Dimension(200, 50));
        return button;
    }

    private void viewInventory() {
        JSONParser parser = new JSONParser();
        File file = new File("inventory.json");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(this, "Inventory file is missing or empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONObject inventoryObject = (JSONObject) parser.parse(reader);

            JTextArea inventoryTextArea = new JTextArea();
            inventoryTextArea.setEditable(false);
            inventoryTextArea.setLineWrap(true);
            inventoryTextArea.setWrapStyleWord(true);
            inventoryTextArea.setFont(new Font("Serif", Font.PLAIN, 18));
            inventoryTextArea.setForeground(Color.BLACK); // Set text color to black

            inventoryTextArea.append("Inventory:\n");
            for (Object key : inventoryObject.keySet()) {
                String ingredient = (String) key;
                double quantity = ((Number) inventoryObject.get(ingredient)).doubleValue(); // Parse quantity as double
                inventoryTextArea.append(ingredient + ": " + quantity + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(inventoryTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "View Inventory", JOptionPane.PLAIN_MESSAGE);

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading inventory file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void editInventory() {
        JSONParser parser = new JSONParser();
        File file = new File("inventory.json");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(this, "Inventory file is missing or empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONObject inventoryObject = (JSONObject) parser.parse(reader);

            JTextArea inventoryTextArea = new JTextArea();
            inventoryTextArea.setEditable(true);
            inventoryTextArea.setLineWrap(true);
            inventoryTextArea.setWrapStyleWord(true);
            inventoryTextArea.setFont(new Font("Serif", Font.PLAIN, 18));
            inventoryTextArea.setForeground(Color.BLACK); // Set text color to black

            inventoryTextArea.append("Inventory:\n");
            for (Object key : inventoryObject.keySet()) {
                String ingredient = (String) key;
                double quantity = ((Number) inventoryObject.get(ingredient)).doubleValue(); // Parse quantity as double
                inventoryTextArea.append(ingredient + ": " + quantity + "\n");
            }

            JScrollPane scrollPane = new JScrollPane(inventoryTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            int option = JOptionPane.showConfirmDialog(this, scrollPane, "Edit Inventory", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (FileWriter fileWriter = new FileWriter("inventory.json")) {
                    String[] lines = inventoryTextArea.getText().split("\n");
                    JSONObject updatedInventory = new JSONObject();
                    for (String line : lines) {
                        if (line.contains(":")) {
                            String[] parts = line.split(":");
                            if (parts.length == 2) {
                                updatedInventory.put(parts[0].trim(), Double.parseDouble(parts[1].trim()));
                            }
                        }
                    }
                    fileWriter.write(updatedInventory.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error writing inventory file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading inventory file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    private void editMenu() {
        JSONParser parser = new JSONParser();
        File file = new File("menu.json");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(this, "Menu file is missing or empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONObject menuJSON = (JSONObject) parser.parse(reader);
            JSONArray menuItemsArray = (JSONArray) menuJSON.get("menuItems");

            JTextArea menuTextArea = new JTextArea();
            menuTextArea.setEditable(true);
            menuTextArea.setLineWrap(true);
            menuTextArea.setWrapStyleWord(true);
            menuTextArea.setFont(new Font("Serif", Font.PLAIN, 18));
            menuTextArea.setForeground(Color.BLACK); // Set text color to black

            for (Object obj : menuItemsArray) {
                JSONObject menuItemJSON = (JSONObject) obj;
                String name = (String) menuItemJSON.get("name");
                String description = (String) menuItemJSON.get("description");
                double price = ((Number) menuItemJSON.get("price")).doubleValue();

                menuTextArea.append(name + " - " + description + " - $" + price + "\n");
                menuTextArea.append("Recipe:\n");
                JSONObject recipe = (JSONObject) menuItemJSON.get("recipe");
                for (Object key : recipe.keySet()) {
                    String ingredient = (String) key;
                    double quantity = ((Number) recipe.get(ingredient)).doubleValue();
                    menuTextArea.append(ingredient + ": " + quantity + "\n");
                }
                menuTextArea.append("\n");
            }

            JScrollPane scrollPane = new JScrollPane(menuTextArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            int option = JOptionPane.showConfirmDialog(this, scrollPane, "Edit Menu", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try (FileWriter fileWriter = new FileWriter("menu.json")) {
                    JSONArray updatedMenuItemsArray = new JSONArray();
                    String[] lines = menuTextArea.getText().split("\n");

                    for (int i = 0; i < lines.length; i++) {
                        if (lines[i].contains(" - $")) {
                            String[] parts = lines[i].split(" - ");
                            if (parts.length == 3) {
                                JSONObject updatedMenuItem = new JSONObject();
                                updatedMenuItem.put("name", parts[0].trim());
                                updatedMenuItem.put("description", parts[1].trim());
                                updatedMenuItem.put("price", Double.parseDouble(parts[2].substring(1).trim())); // Remove $ sign

                                JSONObject updatedRecipe = new JSONObject();
                                while (++i < lines.length && lines[i].contains(":")) {
                                    String[] recipeParts = lines[i].split(":");
                                    if (recipeParts.length == 2) {
                                        updatedRecipe.put(recipeParts[0].trim(), Double.parseDouble(recipeParts[1].trim()));
                                    }
                                }
                                updatedMenuItem.put("recipe", updatedRecipe);
                                updatedMenuItemsArray.add(updatedMenuItem);
                            }
                        }
                    }
                    JSONObject updatedMenu = new JSONObject();
                    updatedMenu.put("menuItems", updatedMenuItemsArray);
                    fileWriter.write(updatedMenu.toJSONString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading menu file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewOrders() {
        JSONParser parser = new JSONParser();
        File file = new File("orders.json");

        if (!file.exists() || file.length() == 0) {
            JOptionPane.showMessageDialog(this, "Orders file is missing or empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (FileReader reader = new FileReader(file)) {
            JSONObject ordersJSON = (JSONObject) parser.parse(reader);
            JSONArray ordersArray = (JSONArray) ordersJSON.get("orders");

            tableModel.setRowCount(0); // Clear existing data

            for (Object obj : ordersArray) {
                JSONObject orderJSON = (JSONObject) obj;
                String orderId = (String) orderJSON.get("orderId");
                String items = (String) orderJSON.get("items");
                String status = (String) orderJSON.get("status");

                tableModel.addRow(new Object[]{orderId, items, status});
            }

        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading orders file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
	private void updateOrderStatus() {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newStatus = JOptionPane.showInputDialog(this, "Enter new status:");
        if (newStatus != null && !newStatus.trim().isEmpty()) {
            tableModel.setValueAt(newStatus, selectedRow, 2); // Update status in table

            // Save changes to the file
            JSONParser parser = new JSONParser();
            File file = new File("orders.json");

            try (FileReader reader = new FileReader(file)) {
                JSONObject ordersJSON = (JSONObject) parser.parse(reader);
                JSONArray ordersArray = (JSONArray) ordersJSON.get("orders");

                String orderId = (String) tableModel.getValueAt(selectedRow, 0);

                for (Object obj : ordersArray) {
                    JSONObject orderJSON = (JSONObject) obj;
                    if (orderJSON.get("orderId").equals(orderId)) {
                        orderJSON.put("status", newStatus);
                        break;
                    }
                }

                try (FileWriter writer = new FileWriter(file)) {
                    ordersJSON.put("orders", ordersArray);
                    writer.write(ordersJSON.toJSONString());
                }

            } catch (IOException | ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating orders file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Status cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RestaurantManager().setVisible(true));
    }
}
