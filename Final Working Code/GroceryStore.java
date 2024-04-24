import java.util.*;

class GroceryStore {
    private String name;
    private Map<GroceryItem, Integer> inventory;
    private Map<GroceryItem, Integer> purchases;

    public GroceryStore(String name) {
        this.name = name;
        this.inventory = new HashMap<>();
        this.purchases = new HashMap<>();

        inventory.put(new GroceryItem("Apple", 0.5), 100);
        inventory.put(new GroceryItem("Banana", 0.25), 50);
        inventory.put(new GroceryItem("Milk", 2.0), 20);
    }

    public String getName() {
        return name;
    }

    public Map<GroceryItem, Integer> getInventory() {
        return inventory;
    }

    public void processList(GroceryList list) {
        for (GroceryItemOrder order : list.orders) {
            GroceryItem item = order.getItem();
            int quantity = order.getQuantity();
            // Check if the item already exists in purchases, if so, add to existing quantity
            if (purchases.containsKey(item)) {
                purchases.put(item, purchases.get(item) + quantity);
            } else {
                purchases.put(item, quantity);
            }
            inventory.put(item, inventory.get(item) - quantity);
        }
    }



    public int getTotalPurchases(GroceryItem item) {
        return purchases.getOrDefault(item, 0);
    }

    public void printSummary() {

        System.out.println("Purchase Summary for " + name + ":");
        List<GroceryItem> sortedItems = new ArrayList<>(purchases.keySet());
        sortedItems.sort(Comparator.comparing(GroceryItem::getName));
        for (GroceryItem item : sortedItems) {
            System.out.println(item.getName() + ": " + purchases.get(item));
            System.out.println();
        }
    }

    public Map<GroceryItem, Integer> getPurchases() {
        return purchases;
    }


}