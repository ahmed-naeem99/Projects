package restaurantManagementSystem;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

public class Inventory {
    private Map<String, Double> stockItems;

    public Inventory() {
        stockItems = new HashMap<>();
        initializeInventory();
    }

    private void initializeInventory() {
        String csvFile = "inventory.txt";
        boolean headerLine = true;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (headerLine == true) {
                    headerLine = false;
                    continue;
                }
                // Split the line by comma, considering quoted values
                String[] values = line.split(",");

                // Trim and remove double quotes from each value
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].trim().replaceAll("^\"|\"$", "");
                }

                // Assuming the first attribute is a string and the second is a number
                String stringValue = values[0];
                double numberValue = Double.parseDouble(values[1]);

                stockItems.put(stringValue, numberValue);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void takeFromInventory(String stockItem, double quantity) {
        double stockQuantity = stockItems.get(stockItem);
        stockQuantity -= quantity;
        stockItems.put(stockItem, stockQuantity);
    }

    public Map<String, Double> getIngredients() {
        return stockItems;
    }

    public void saveInventory() {
        String csvFile = "inventory.txt";

        try (Writer writer = new FileWriter(csvFile)) {

            // Write header row
            writer.write("Item, Quantity\n");

            // Write data rows
            for (String stockItem : stockItems.keySet()) {
                writer.write("\"" + stockItem + "\", \"" + stockItems.get(stockItem) + "\"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToInventory(String stockItem, double quantity) {
        double stockQuantity = stockItems.get(stockItem);
        stockQuantity += quantity;
        stockItems.put(stockItem, stockQuantity);
    }

    public boolean useIngredients(Map<String, Double> recipe) {
        // Check if there are enough ingredients in stock
        for (Map.Entry<String, Double> entry : recipe.entrySet()) {
            String ingredient = entry.getKey();
            Double requiredQuantity = entry.getValue();
            Double availableQuantity = stockItems.getOrDefault(ingredient, 0.0);
            if (availableQuantity < requiredQuantity) {
                return false; // Not enough ingredients
            }
        }

        // Deduct the ingredients from stock
        for (Map.Entry<String, Double> entry : recipe.entrySet()) {
            String ingredient = entry.getKey();
            Double requiredQuantity = entry.getValue();
            stockItems.put(ingredient, stockItems.get(ingredient) - requiredQuantity);
        }

        return true;
    }
    
    public Map<String, Double> getStockItems() {
        return stockItems;
    }

    @SuppressWarnings("unchecked")
    public void saveInventoryToFile() {
        JSONObject inventoryJSON = new JSONObject();
        for (Map.Entry<String, Double> entry : stockItems.entrySet()) {
            inventoryJSON.put(entry.getKey(), entry.getValue());
        }

        try (FileWriter file = new FileWriter("inventory.json")) {
            file.write(inventoryJSON.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
	