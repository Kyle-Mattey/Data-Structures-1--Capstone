import java.util.ArrayList;
import java.util.List;

class GroceryList {
    List<GroceryItemOrder> orders;
    private double discount;


    public GroceryList(double discount) {
        this.orders = new ArrayList<>();
        this.discount = discount;
    }

    public void addOrder(GroceryItemOrder order) {
        orders.add(order);
    }

    public List<GroceryItemOrder> getOrders() {
        return orders;
    }


    public double getSubtotal() {
        double subtotal = 0;
        for (GroceryItemOrder order : orders) {
            subtotal += order.getTotalCost();
        }
        return subtotal;
    }

    public double getTotalCost() {
        return getSubtotal() * (1 - discount);
    }

    public void printContents() {
        System.out.println(" ");
        System.out.println("Grocery List Contents:");
        for (GroceryItemOrder order : orders) {
            System.out.println(order.getQuantity() + " x " + order.getItem().getName() + " @ $" + order.getItem().getPrice());
        }
        System.out.println("Subtotal: $" + getSubtotal());


    }
}
