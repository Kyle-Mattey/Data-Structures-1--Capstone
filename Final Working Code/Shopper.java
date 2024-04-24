class Shopper {
    private Person person;
    private GroceryList groceryList;

    public Shopper(Person person, GroceryList groceryList) {
        this.person = person;
        this.groceryList = groceryList;
    }

    public Person getPerson() {
        return person;
    }

    public GroceryList getGroceryList() {
        return groceryList;
    }
}