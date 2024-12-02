package restaurantManagementSystem;

import javax.swing.*;
import java.awt.*;

public class MainSystem extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Font WELCOME_FONT = new Font("Serif", Font.BOLD, 36);
    private static final Font FONT = new Font("Serif", Font.PLAIN, 30);

    public MainSystem() {
        setTitle("Restaurant Management System Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Set the initial size
        setLocationRelativeTo(null); // Center the window on the screen

        JPanel panel = new JPanel(new BorderLayout());

        // Welcome message label
        JLabel label = new JLabel("Welcome to Restaurant Management System");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(WELCOME_FONT); // Change font and size
        panel.add(label, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 20, 20)); // 4 rows, 1 column, 20px gaps

        JButton customerButton = createStyledButton("Customer");
        JButton waiterButton = createStyledButton("Waiter");
        JButton chefButton = createStyledButton("Chef");
        JButton managerButton = createStyledButton("Restaurant Manager");

        // Add action listeners to the buttons
        customerButton.addActionListener(e -> openWindow(new Customer()));
        waiterButton.addActionListener(e -> openWindow(new Waiter()));
        chefButton.addActionListener(e -> openWindow(new Chef()));
        managerButton.addActionListener(e -> openWindow(new RestaurantManager()));

        // Add buttons to button panel
        buttonPanel.add(customerButton);
        buttonPanel.add(waiterButton);
        buttonPanel.add(chefButton);
        buttonPanel.add(managerButton);

        // Add borders around the buttons
        customerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        waiterButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chefButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        managerButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        panel.add(buttonPanel, BorderLayout.CENTER);
        add(panel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);
        button.setBackground(new Color(199, 0, 57)); // Change button background color
        button.setForeground(Color.WHITE); // Set text color to white
        button.setFont(FONT); // Change font and size
        button.setPreferredSize(new Dimension(200, 100)); // Set a preferred size for the buttons
        return button;
    }

    private void openWindow(JFrame window) {
        window.setVisible(true);
        dispose(); // Close login window
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create and display the main login window
            MainSystem mainSystem = new MainSystem();
            mainSystem.setVisible(true);
        });
    }
}