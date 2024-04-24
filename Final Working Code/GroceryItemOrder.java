class GroceryItemOrder {
    private GroceryItem item;
    private int quantity;

    public GroceryItemOrder(GroceryItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public GroceryItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return item.getPrice() * quantity;
    }
}