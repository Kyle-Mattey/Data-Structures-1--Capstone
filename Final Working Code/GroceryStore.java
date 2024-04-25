import java.util.*;

class GroceryStore {
    private String name;
    private Map<GroceryItem, Integer> inventory;
    private Map<GroceryItem, Integer> purchases;
    private Map<GroceryList, Double> afterDiscountTotals;

    public GroceryStore(String name) {
        this.name = name;
        this.inventory = new HashMap<>();
        this.purchases = new HashMap<>();

        inventory.put(new GroceryItem("Apple", 0.5), 100);
        inventory.put(new GroceryItem("Banana", 0.25), 50);
        inventory.put(new GroceryItem("Milk", 2.0), 20);
        this.afterDiscountTotals = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void storeAfterDiscountTotal(GroceryList groceryList, double afterDiscountTotal) {
        afterDiscountTotals.put(groceryList, afterDiscountTotal);
    }

    public Map<GroceryList, Double> getAfterDiscountTotals() {
        return afterDiscountTotals;
    }

    public Map<GroceryItem, Integer> getInventory() {
        return inventory;
    }

    public void processList(GroceryList list) {
        for (GroceryItemOrder order : list.orders) {
            GroceryItem item = order.getItem();
            int quantity = order.getQuantity();
            purchases.put(item, quantity);
            inventory.put(item, inventory.get(item) - quantity);
        }
        double afterDiscountTotal = list.getTotalCost();
        storeAfterDiscountTotal(list, afterDiscountTotal);
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
