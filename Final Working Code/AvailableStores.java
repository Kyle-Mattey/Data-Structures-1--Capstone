import java.util.*;

class AvailableStores {
    //This will list to store all the grocery stores available
    private static List<GroceryStore> stores = new ArrayList<>();

    //This will add the grocery stores to the list
    static {
        stores.add(new GroceryStore("Target Rogers Park"));
        stores.add(new GroceryStore("Target at Wilson"));
    }

    //Method to get the list of available stores
    public static List<GroceryStore> getStores() {
        return stores;
    }
}