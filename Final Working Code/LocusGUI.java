import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

public class LocusGUI extends JFrame {
    private Locus locus;
    private JTextArea outputArea;
    private JPanel mainPanel;
    private Person currentPerson;

    public LocusGUI() {
        super("Locus");
        locus = new Locus();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new BorderLayout());

        // Create buttons for managing classes, grocery shopping, and grocery processing
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton manageClassesButton = new JButton("Manage Classes");
        manageClassesButton.addActionListener(e -> manageClasses());
        buttonPanel.add(manageClassesButton);

        JButton goGroceryShoppingButton = new JButton("Go Grocery Shopping");
        goGroceryShoppingButton.addActionListener(e -> goGroceryShopping());
        buttonPanel.add(goGroceryShoppingButton);

        JButton groceryProcessingButton = new JButton("Grocery Processing");
        groceryProcessingButton.addActionListener(e -> groceryProcessing());
        buttonPanel.add(groceryProcessingButton);

        mainPanel.add(buttonPanel, BorderLayout.WEST);

        // Create a text area for displaying output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
        createUser();
    }

    // Create a new user (person or student)
    private void createUser() {
        String firstName = showInputDialog("Enter your first name:");
        String lastName = showInputDialog("Enter your last name:");
        boolean isStudent = showConfirmDialog("Are you a student?");

        if (isStudent) {
            currentPerson = new Student(firstName, lastName);
        } else {
            currentPerson = new Person(firstName, lastName);
        }

        locus.addPerson(currentPerson);
        appendOutput("User created: " + currentPerson.getFullName() + (isStudent ? " (Student)" : " (Non-Student)"));
    }

    // Manage classes for students
    private void manageClasses() {
        if (!(currentPerson instanceof Student)) {
            appendOutput("Only students can manage classes.");
            return;
        }

        Student student = (Student) currentPerson;
        List<Course> courses = locus.getCourses();

        while (true) {
            String input = showInputDialog("Enter your choice:\n1. Register for a Course\n2. Drop a Course\n3. View Courses\n4. Exit");
            switch (input) {
                case "1":
                    registerCourse(student, courses);
                    break;
                case "2":
                    dropCourse(student, courses);
                    break;
                case "3":
                    viewCourses(student);
                    break;
                case "4":
                    appendOutput("Exiting Manage Classes");
                    return;
                default:
                    appendOutput("Invalid choice");
            }
        }
    }

    // Register a student for a course
    private void registerCourse(Student student, List<Course> courses) {
        appendOutput("Available Courses:");
        for (int i = 0; i < courses.size(); i++) {
            appendOutput((i + 1) + ". " + courses.get(i).getName());
        }

        String input = showInputDialog("Enter the course number to register:");
        int courseChoice;
        try {
            courseChoice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            appendOutput("Invalid course choice");
            return;
        }

        if (courseChoice < 1 || courseChoice > courses.size()) {
            appendOutput("Invalid course choice");
            return;
        }

        Course course = courses.get(courseChoice - 1);
        locus.registerStudent(student, course);
        appendOutput("Registered for " + course.getName());
    }

    // Drop a student from a course
    private void dropCourse(Student student, List<Course> courses) {
        String courseName = showInputDialog("Enter the course name to drop:");
        Course course = findCourse(courses, courseName);
        if (course != null) {
            locus.dropStudent(student, course);
            appendOutput("Dropped " + course.getName());
        } else {
            appendOutput("Course not found");
        }
    }

    // View courses a student is enrolled in
    private void viewCourses(Student student) {
        List<Course> enrolledCourses = student.getCourses();
        if (enrolledCourses.isEmpty()) {
            appendOutput("You are not enrolled in any courses.");
        } else {
            appendOutput("Enrolled Courses:");
            for (Course course : enrolledCourses) {
                appendOutput("- " + course.getName());
            }
        }
    }

    // Find a course by name
    private Course findCourse(List<Course> courses, String courseName) {
        for (Course course : courses) {
            if (course.getName().equalsIgnoreCase(courseName)) {
                return course;
            }
        }
        return null;
    }

    // Go grocery shopping
    private void goGroceryShopping() {
        List<GroceryStore> stores = locus.getStores();
        JComboBox<String> storeComboBox = new JComboBox<>();
        for (GroceryStore store : stores) {
            storeComboBox.addItem(store.getName());
        }

        // Create a dialog box with the combo box
        int result = JOptionPane.showConfirmDialog(this, storeComboBox, "Select a Store", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String selectedStoreName = (String) storeComboBox.getSelectedItem();
            GroceryStore selectedStore = findStore(stores, selectedStoreName);
            if (selectedStore != null) {
                createGroceryList(selectedStore);
            } else {
                appendOutput("Store not found.");
            }
        } else {
            appendOutput("Store selection canceled.");
        }
    }

    // Helper method to find a store by name
    private GroceryStore findStore(List<GroceryStore> stores, String storeName) {
        for (GroceryStore store : stores) {
            if (store.getName().equals(storeName)) {
                return store;
            }
        }
        return null;
    }

    // Create a grocery list
    private void createGroceryList(GroceryStore store) {
        double discount = currentPerson instanceof Student ? 0.1 : 0.0;
        GroceryList groceryList = new GroceryList(discount);
        Map<GroceryItem, Integer> inventory = store.getInventory();
        List<GroceryItem> sortedItems = new ArrayList<>(inventory.keySet());
        sortedItems.sort(Comparator.comparing(GroceryItem::getName));

        while (true) {
            appendOutput("\nItems available at " + store.getName() + ":");
            for (int i = 0; i < sortedItems.size(); i++) {
                GroceryItem item = sortedItems.get(i);
                appendOutput((i + 1) + ". " + item.getName() + " - $" + item.getPrice() + " (Quantity: " + inventory.get(item) + ")");
            }

            String input = showInputDialog("Enter the item number to add to cart (0 to finish):");
            int itemChoice;
            try {
                itemChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                appendOutput("Invalid item choice");
                continue;
            }

            if (itemChoice == 0) {
                break;
            } else if (itemChoice < 1 || itemChoice > sortedItems.size()) {
                appendOutput("Invalid item choice");
                continue;
            }

            GroceryItem selectedItem = sortedItems.get(itemChoice - 1);
            int quantity = getQuantity(selectedItem, inventory.get(selectedItem));
            if (quantity > 0) {
                groceryList.addOrder(new GroceryItemOrder(selectedItem, quantity));
                appendOutput("Added " + quantity + " " + selectedItem.getName() + "(s) to cart.");
            }
        }

        // Display shopping cart summary
        appendOutput("\nShopping Cart Summary:");
        for (GroceryItemOrder order : groceryList.getOrders()) {
            GroceryItem item = order.getItem();
            int quantity = order.getQuantity();
            double totalCost = order.getTotalCost();
            appendOutput(item.getName() + " - Quantity: " + quantity + ", Total Cost: $" + totalCost);
        }

        // Display price breakdown
        double subtotal = groceryList.getSubtotal();
        appendOutput("\nSubtotal: $" + subtotal);
        if (currentPerson instanceof Student) {
            double studentDiscount = subtotal * discount;
            appendOutput("Student Discount: $" + String.format("%.2f", studentDiscount));
        }

        double total = groceryList.getTotalCost();
        appendOutput("Total: $" + String.format("%.2f", total));

        Shopper shopper = new Shopper(currentPerson, groceryList);
        store.processList(groceryList);
        locus.processGroceryList(shopper, store);
        appendOutput("\nGrocery list processed successfully.");
    }

    // Get the quantity of an item to add to the grocery list
    private int getQuantity(GroceryItem item, int availableQuantity) {
        while (true) {
            String input = showInputDialog("Enter the quantity for " + item.getName() + " (Available: " + availableQuantity + "):");
            int quantity;
            try {
                quantity = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                appendOutput("Invalid quantity");
                continue;
            }

            if (quantity < 1 || quantity > availableQuantity) {
                appendOutput("Invalid quantity");
                continue;
            }

            return quantity;
        }
    }

    // Process grocery purchases
    private void groceryProcessing() {
        while (true) {
            String input = showInputDialog("What would you like to view?\n1. Total Revenue\n2. Store Revenue\n3. Item Revenue\n4. Exit");
            switch (input) {
                case "1":
                    printTotalRevenue();
                    break;
                case "2":
                    printStoreRevenue();
                    break;
                case "3":
                    printItemRevenue();
                    break;
                case "4":
                    appendOutput("Exiting Grocery Processing");
                    return;
                default:
                    appendOutput("Invalid choice");
            }
        }
    }

    // Print total revenue from all stores
    private void printTotalRevenue() {
        double totalRevenue = 0;
        for (GroceryStore store : locus.getStores()) {
            for (Map.Entry<GroceryItem, Integer> entry : store.getPurchases().entrySet()) {
                GroceryItem item = entry.getKey();
                int quantity = entry.getValue();
                totalRevenue += item.getPrice() * quantity;
            }
        }
        appendOutput("\n--- Total Revenue ---");
        appendOutput("Total Revenue: $" + totalRevenue);
    }

    // Print revenue for each store
    private void printStoreRevenue() {
        List<GroceryStore> stores = locus.getStores();
        StringBuilder storeMenu = new StringBuilder();
        storeMenu.append("Select a store to view revenue:\n");
        for (int i = 0; i < stores.size(); i++) {
            storeMenu.append((i + 1) + ". " + stores.get(i).getName() + "\n");
        }
        storeMenu.append((stores.size() + 1) + ". Exit");

        while (true) {
            String input = showInputDialog(storeMenu.toString());
            int storeChoice;
            try {
                storeChoice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                appendOutput("Invalid store choice");
                continue;
            }

            if (storeChoice < 1 || storeChoice > stores.size() + 1) {
                appendOutput("Invalid store choice");
                continue;
            }

            if (storeChoice == stores.size() + 1) {
                appendOutput("Exiting Store Revenue");
                return;
            }

            GroceryStore selectedStore = stores.get(storeChoice - 1);
            appendOutput("\n--- " + selectedStore.getName() + " Revenue ---");
            selectedStore.printSummary();

            double storeRevenue = calculateStoreRevenue(selectedStore);
            appendOutput("Total Revenue for " + selectedStore.getName() + ": $" + storeRevenue);
        }
    }

    // Calculate revenue for a specific store
    private double calculateStoreRevenue(GroceryStore store) {
        double revenue = 0;
        for (Map.Entry<GroceryItem, Integer> entry : store.getPurchases().entrySet()) {
            GroceryItem item = entry.getKey();
            int quantity = entry.getValue();
            revenue += item.getPrice() * quantity;
        }
        return revenue;
    }

    // Print revenue for each item
    private void printItemRevenue() {
        appendOutput("\n--- Item Revenue ---");

        Map<String, Double> itemRevenueMap = new HashMap<>();

        for (GroceryStore store : locus.getStores()) {
            for (Map.Entry<GroceryItem, Integer> entry : store.getPurchases().entrySet()) {
                GroceryItem item = entry.getKey();
                int quantity = entry.getValue();

                String itemStoreKey = item.getName() + "-" + store.getName();
                double revenue = item.getPrice() * quantity;
                itemRevenueMap.put(itemStoreKey, itemRevenueMap.getOrDefault(itemStoreKey, 0.0) + revenue);
            }
        }

        int itemCount = 1;
        for (Map.Entry<String, Double> entry : itemRevenueMap.entrySet()) {
            String itemStoreKey = entry.getKey();
            double revenue = entry.getValue();

            String[] parts = itemStoreKey.split("-");
            String itemName = parts[0];
            String storeName = parts[1];

            appendOutput(itemCount + ". " + itemName + " from " + storeName);
            itemCount++;
        }

        String input = showInputDialog("Enter the item number to view revenue:");
        int itemChoice;
        try {
            itemChoice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            appendOutput("Invalid item choice");
            return;
        }

        if (itemChoice < 1 || itemChoice > itemRevenueMap.size()) {
            appendOutput("Invalid item choice");
        } else {
            String selectedKey = (String) itemRevenueMap.keySet().toArray()[itemChoice - 1];
            double selectedRevenue = itemRevenueMap.get(selectedKey);

            String[] parts = selectedKey.split("-");
            String selectedItem = parts[0];
            String selectedStore = parts[1];

            appendOutput("Revenue for " + selectedItem + " from " + selectedStore + ": $" + selectedRevenue);
        }
    }

    // Helper methods for user input and output
    private String showInputDialog(String message) {
        return JOptionPane.showInputDialog(this, message);
    }

    private boolean showConfirmDialog(String message) {
        int choice = JOptionPane.showConfirmDialog(this, message, "Confirmation", JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private void appendOutput(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LocusGUI());
    }
}
