package restaurantManagementSystem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import restaurantManagementSystem.OrderList.Order;
import restaurantManagementSystem.OrderList.Order.OrderStatus;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Waiter extends JFrame {

    private static final long serialVersionUID = 1L;
    private JLabel welcomeLabel;
    private JTable ordersTable;
    private OrderTableModel orderTableModel;
    private JButton searchButton;

    public Waiter() {
        setTitle("Waiter Window");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        welcomeLabel = new JLabel("Welcome, Waiter!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 24));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(welcomeLabel, BorderLayout.NORTH);

        orderTableModel = new OrderTableModel(new ArrayList<>());
        ordersTable = new JTable(orderTableModel);
        ordersTable.setRowHeight(30);

        ordersTable.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        ordersTable.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox(), orderTableModel));

        panel.add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        searchButton = new JButton("Search Order");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrder();
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(searchButton);

        panel.add(buttonPanel, BorderLayout.NORTH);
        add(panel);

        promptLogin();
    }

    private void promptLogin() {
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        String password = JOptionPane.showInputDialog(this, "Enter password:");
        if (isValidLogin(username, password)) {
            welcomeLabel.setText("Welcome, " + username + "!");
            loadAndDisplayOrders();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.");
            // Retry login if invalid credentials
            promptLogin();
        }
    }

    private void searchOrder() {
        String searchTerm = JOptionPane.showInputDialog(this, "Enter customer name or number:");
        if (searchTerm == null || searchTerm.isEmpty()) {
            return; // Return if search term is empty or user cancels
        }

        Order foundOrder = findOrder(searchTerm);
        if (foundOrder != null) {
            displayOrderPopup(foundOrder);
        } else {
            JOptionPane.showMessageDialog(this, "No order found for " + searchTerm);
        }
    }

    protected Order findOrder(String searchTerm) {
        // Loop through orders to find a matching order
        for (Order order : orderTableModel.orders) {
            // Check if search term matches customer name or number
            if (order.getCustomerName().equalsIgnoreCase(searchTerm) ||
                    order.getCustomerNumber().equals(searchTerm)) {
                return order; // Return the found order
            }
        }
        return null; // Return null if no order is found
    }

    private void displayOrderPopup(Order order) {
        // Create a formatted message to display the order details
        StringBuilder message = new StringBuilder();
        message.append("Order ID: ").append(order.getId()).append("\n");
        message.append("Customer Name: ").append(order.getCustomerName()).append("\n");
        message.append("Customer Number: ").append(order.getCustomerNumber()).append("\n");
        message.append("Order Date & Time: ").append(order.getOrderDateTime().toString()).append("\n\n");

        message.append("Order Items:\n");
        double subtotal = 0.0;
        for (MenuItem item : order.getItems()) {
            double itemTotal = item.getPrice() * item.getQuantity();
            subtotal += itemTotal;
            message.append(item.getName())
                   .append(" (x").append(item.getQuantity()).append(") - $")
                   .append(String.format("%.2f", itemTotal)).append("\n");
        }

        double tax = subtotal * 0.13; // Assuming a 13% tax rate
        double total = subtotal + tax;

        message.append("\nSubtotal: $").append(String.format("%.2f", subtotal)).append("\n");
        message.append("Tax: $").append(String.format("%.2f", tax)).append("\n");
        message.append("Total: $").append(String.format("%.2f", total)).append("\n");
        message.append("Delivery: ").append(order.isDelivery() ? "Yes" : "No").append("\n");

        // Show a popup with the order details
        JOptionPane.showMessageDialog(this, message.toString(), "Order Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadAndDisplayOrders() {
        List<Order> orders = loadOrdersFromFile();
        orderTableModel.setOrders(orders);
    }

    protected List<Order> loadOrdersFromFile() {
        List<Order> orders = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("RestaurantOrders.json")) {
            JSONArray ordersJSON = (JSONArray) parser.parse(reader);

            for (Object obj : ordersJSON) {
                JSONObject orderJSON = (JSONObject) obj;
                JSONArray itemsArray = (JSONArray) orderJSON.get("items");
                List<MenuItem> items = new ArrayList<>();

                if (itemsArray != null) {
                    for (Object itemObj : itemsArray) {
                        JSONObject itemJSON = (JSONObject) itemObj;
                        String name = (String) itemJSON.get("name");
                        Number priceNumber = (Number) itemJSON.get("price");
                        Number quantityNumber = (Number) itemJSON.get("quantity");
                        double price = priceNumber != null ? priceNumber.doubleValue() : 0.0;
                        int quantity = quantityNumber != null ? quantityNumber.intValue() : 0;
                        items.add(new MenuItem(name, "", price, new HashMap<>(), quantity));
                    }
                }

                Number idNumber = (Number) orderJSON.get("id");
                int id = idNumber != null ? idNumber.intValue() : -1;
                Boolean delivery = (Boolean) orderJSON.get("delivery");
                String paymentMethod = (String) orderJSON.get("paymentMethod");
                String status = (String) orderJSON.get("status");
                String customerName = (String) orderJSON.get("customerName");
                String customerNumber = (String) orderJSON.get("customerNumber");

                Order order = new Order(items, delivery != null && delivery, paymentMethod != null ? paymentMethod : "", 
                                        customerName != null ? customerName : "", 
                                        customerNumber != null ? customerNumber : "");
                
                if (status != null) {
                    order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
                } else {
                    order.setStatus(OrderStatus.PLACED);
                }

                Field idField = Order.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(order, id);

                orders.add(order);
            }
        } catch (IOException | ParseException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return orders;
    }
    
    @SuppressWarnings("unchecked") 
    protected void updateOrderStatusInFile(Order order) {
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("RestaurantOrders.json")) {
            JSONArray ordersJSON = (JSONArray) parser.parse(reader);

            for (Object obj : ordersJSON) {
                JSONObject orderJSON = (JSONObject) obj;
                int id = ((Number) orderJSON.get("id")).intValue();

                if (id == order.getId()) {
                    orderJSON.put("status", order.getStatus().name());
                    break;
                }
            }

            try (FileWriter writer = new FileWriter("RestaurantOrders.json")) {
                writer.write(ordersJSON.toJSONString());
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    protected boolean isValidLogin(String username, String password) {
        return username.equals("WAITER") && password.equals("server123");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Waiter().setVisible(true);
            }
        });
    }

    public class OrderTableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;
        private List<Order> orders;
        private final String[] columnNames = {"Order ID", "Customer Name", "Status", "Actions"};

        public OrderTableModel(List<Order> orders) {
            this.orders = orders;
        }

        public void setOrders(List<Order> orders) {
            this.orders = orders;
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return orders.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return order.getId();
                case 1:
                    return order.getCustomerName();
                case 2:
                    return order.getStatus().toString();
                case 3:
                    return "Delivered"; // Show "Delivered" for actions column
                default:
                    return null;
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3; // Only the Actions column is editable
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {

        private static final long serialVersionUID = 1L;

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {

        private static final long serialVersionUID = 1L;
        private JButton button;
        private String label;
        private boolean isPushed;
        private OrderTableModel orderTableModel;

        public ButtonEditor(JCheckBox checkBox, OrderTableModel orderTableModel) {
            super(checkBox);
            this.orderTableModel = orderTableModel;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int row = ordersTable.getSelectedRow();
                Order order = orderTableModel.orders.get(row);
                order.setStatus(OrderStatus.DELIVERED);
                updateOrderStatusInFile(order);
                saveOrdersToFile(orderTableModel.orders);
                orderTableModel.fireTableRowsUpdated(row, row);
            }
            isPushed = false;
            return label;
        }

        @SuppressWarnings("unchecked")
		private void saveOrdersToFile(List<Order> orders) {
            JSONArray ordersJSON = new JSONArray();
            for (Order order : orders) {
                JSONObject orderJSON = new JSONObject();
                orderJSON.put("id", order.getId());
                orderJSON.put("delivery", order.isDelivery());
                orderJSON.put("paymentMethod", order.getPaymentMethod());
                orderJSON.put("status", order.getStatus().toString());
                orderJSON.put("customerName", order.getCustomerName());
                orderJSON.put("customerNumber", order.getCustomerNumber());

                JSONArray itemsArray = new JSONArray();
                for (MenuItem item : order.getItems()) {
                    JSONObject itemJSON = new JSONObject();
                    itemJSON.put("name", item.getName());
                    itemJSON.put("price", item.getPrice());
                    itemJSON.put("quantity", item.getQuantity());
                    itemsArray.add(itemJSON);
                }
                orderJSON.put("items", itemsArray);
                ordersJSON.add(orderJSON);
            }

            try (FileWriter file = new FileWriter("RestaurantOrders.json")) {
                file.write(ordersJSON.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
