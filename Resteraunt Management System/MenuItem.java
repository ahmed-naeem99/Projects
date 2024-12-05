package restaurantManagementSystem;

import java.util.Map;

public class MenuItem {
    private String name;
    private String description;
    private double price;
    private Map<String, Double> recipe;
    private int quantity;

    public MenuItem(String name, String description, double price, Map<String, Double> recipe, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.recipe = recipe;
        this.quantity = quantity;
    }

    public MenuItem(String name, String description, double price, Map<String, Double> recipe) {
        this(name, description, price, recipe, 1);  // Default quantity
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Map<String, Double> getRecipe() {
        return recipe;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return String.format("%s: %s ($%.2f), Recipe: %s, Quantity: %d", name, description, price, recipe.toString(), quantity);
    }
}