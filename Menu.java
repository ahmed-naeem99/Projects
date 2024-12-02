package restaurantManagementSystem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private List<MenuItem> items;

    public Menu() {
        items = new ArrayList<>();
        loadMenuFromFile();  // Load menu items from the JSON file when Menu is instantiated
    }

    private void loadMenuFromFile() {
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("menu.json")) {
            JSONObject menuJSON = (JSONObject) parser.parse(reader);
            JSONArray menuItemsArray = (JSONArray) menuJSON.get("menuItems");

            for (Object obj : menuItemsArray) {
                JSONObject menuItemJSON = (JSONObject) obj;
                String name = (String) menuItemJSON.get("name");
                String description = (String) menuItemJSON.get("description");
                double price = ((Number) menuItemJSON.get("price")).doubleValue();

                JSONObject recipeJSON = (JSONObject) menuItemJSON.get("recipe");
                Map<String, Double> recipe = new HashMap<>();
                for (Object key : recipeJSON.keySet()) {
                    String ingredient = (String) key;
                    double quantity = ((Number) recipeJSON.get(ingredient)).doubleValue();
                    recipe.put(ingredient, quantity);
                }

                MenuItem item = new MenuItem(name, description, price, recipe, 0);
                addItem(item);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public List<MenuItem> getItems() {
        return items;
    }
}
