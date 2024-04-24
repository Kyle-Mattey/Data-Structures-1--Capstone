import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Locus locus = new Locus();

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Are you a student? (y/n) ");
        boolean isStudent = scanner.nextLine().equalsIgnoreCase("y");

        Person person;
        if (isStudent) {
            person = new Student(firstName, lastName);
        } else {
            person = new Person(firstName, lastName);
        }

        locus.addPerson(person);

        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Manage Classes");
        System.out.println("2. Go Grocery Shopping");
        System.out.println("3. Grocery Processing");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice == 1) {
            manageClasses(locus, person, isStudent, scanner);
        } else if (choice == 2) {
            goGroceryShopping(locus, person, scanner);
        } else if (choice == 3) {
            groceryProcessing(locus, scanner);
        } else {
            System.out.println("Invalid choice.");
        }

        scanner.close();
    }

    static void manageClasses(Locus locus, Person person, boolean isStudent, Scanner scanner) {
        if (isStudent) {
            Student student = (Student) person;
            System.out.println("\nAvailable Courses:");
            List<Course> courses = locus.getCourses();
            for (int i = 0; i < courses.size(); i++) {
                System.out.println((i + 1) + ". " + courses.get(i).getName());
            }

            boolean managing = true;
            while (managing) {
                System.out.println("\nWhat would you like to do?");
                System.out.println("1. Register for a Course");
                System.out.println("2. Drop a Course");
                System.out.println("3. View Courses");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter the course number to register: ");
                        int registerCourseNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (registerCourseNumber < 1 || registerCourseNumber > courses.size()) {
                            System.out.println("Invalid course number.");
                        } else {
                            Course course = courses.get(registerCourseNumber - 1);
                            locus.registerStudent(student, course);
                            System.out.println("Registered for " + course.getName());
                        }
                        break;
                    case 2:
                        System.out.print("Enter the course number to drop: ");
                        int dropCourseNumber = scanner.nextInt();
                        scanner.nextLine();
                        if (dropCourseNumber < 1 || dropCourseNumber > courses.size()) {
                            System.out.println("Invalid course number.");
                        } else {
                            Course course = courses.get(dropCourseNumber - 1);
                            locus.dropStudent(student, course);
                            System.out.println("Dropped " + course.getName());
                        }
                        break;
                    case 3:
                        System.out.println("\nEnrolled Courses:");
                        List<Course> enrolledCourses = student.getCourses();
                        if (enrolledCourses.isEmpty()) {
                            System.out.println("You are not enrolled in any courses.");
                        } else {
                            for (Course course : enrolledCourses) {
                                System.out.println("- " + course.getName());
                            }
                        }
                        break;
                    case 4:
                        managing = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } else {
            System.out.println("Only students can manage classes.");
        }
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Go Grocery Shopping");
        System.out.println("2. Grocery Processing");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                goGroceryShopping(locus, person, scanner);
                break;
            case 2:
                groceryProcessing(locus, scanner);
                break;
            case 3:
                System.exit(0); // Exit the program
            default:
                System.out.println("Invalid choice. Exiting program.");
                System.exit(1); // Exit the program with an error
        }
    }





    static void goGroceryShopping(Locus locus, Person person, Scanner scanner) {
        boolean continueGroceryShopping = true;

        while (continueGroceryShopping) {
            System.out.println("Available Stores:");
            List<GroceryStore> stores = AvailableStores.getStores();
            for (int i = 0; i < stores.size(); i++) {
                System.out.println((i + 1) + ". " + stores.get(i).getName());
            }
            System.out.print("Select a store: ");
            int storeChoice = scanner.nextInt();
            scanner.nextLine();

            GroceryStore selectedStore = stores.get(storeChoice - 1);
            System.out.println("\nItems available at " + selectedStore.getName() + ":");
            Map<GroceryItem, Integer> inventory = selectedStore.getInventory();
            List<GroceryItem> sortedItems = new ArrayList<>(inventory.keySet());
            sortedItems.sort(Comparator.comparing(GroceryItem::getName));
            for (int i = 0; i < sortedItems.size(); i++) {
                GroceryItem item = sortedItems.get(i);
                System.out.println((i + 1) + ". " + item.getName() + " - $" + item.getPrice() + " (Quantity: " + inventory.get(item) + ")");
            }

            boolean isStudent = true;
            GroceryList groceryList = new GroceryList(isStudent ? 0.1 : 0.0);
            boolean addingItems = true;
            while (addingItems) {
                System.out.print("Enter the item number to add to cart (0 to finish): ");
                int itemChoice = scanner.nextInt();
                scanner.nextLine();

                if (itemChoice == 0) {
                    addingItems = false;
                } else if (itemChoice < 1 || itemChoice > sortedItems.size()) {
                    System.out.println("Invalid item number. Please try again.");
                } else {
                    GroceryItem selectedItem = sortedItems.get(itemChoice - 1);
                    System.out.print("Enter the quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    if (quantity > inventory.get(selectedItem)) {
                        System.out.println("Not enough inventory for the requested quantity.");
                    } else {
                        groceryList.addOrder(new GroceryItemOrder(selectedItem, quantity));
                        System.out.println("Added " + quantity + " " + selectedItem.getName() + "(s) to cart.");
                    }

                    System.out.print("Add another item? (y/n) ");
                    String choice = scanner.nextLine();
                    addingItems = choice.equalsIgnoreCase("y");
                }
            }

            Shopper shopper = new Shopper(person, groceryList);
            selectedStore.processList(groceryList); // Update the purchases in the store
            locus.processGroceryList(shopper, selectedStore);


            System.out.println("\nWhat would you like to do now?");
            System.out.println("1. Go Grocery Shopping");
            System.out.println("2. Grocery Processing");
            System.out.println("3. Manage Classes"); // Option to manage classes
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    // Continue grocery shopping
                    break;
                case 2:
                    groceryProcessing(locus, scanner);
                    continueGroceryShopping = false;
                    break;
                case 3:
                    manageClasses(locus, person, person instanceof Student, scanner); // Call the manageClasses method
                    break;
                case 4:
                    continueGroceryShopping = false;
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continueGroceryShopping = false;
            }
        }
    }

    static void groceryProcessing(Locus locus, Scanner scanner) {
        boolean continueProcessing = true;
        while (continueProcessing) {
            System.out.println("\nWhat would you like to view?");
            System.out.println("1. Total Revenue");
            System.out.println("2. Store Revenue");
            System.out.println("3. Item Revenue");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    printTotalRevenue(locus);
                    break;
                case 2:
                    printStoreRevenue(locus);
                    break;
                case 3:
                    printItemRevenue(locus, scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    continueProcessing = false;
            }

            System.out.print("\nContinue processing? (y/n) ");
            String continueChoice = scanner.nextLine();
            continueProcessing = continueChoice.equalsIgnoreCase("y");
        }
    }

    private static void printTotalRevenue(Locus locus) {
        double totalRevenue = 0;
        for (GroceryStore store : locus.getStores()) {
            for (Map.Entry<GroceryItem, Integer> entry : store.getPurchases().entrySet()) {
                GroceryItem item = entry.getKey();
                int quantity = entry.getValue();
                totalRevenue += (item.getPrice() * quantity); // Divide the revenue by 2
            }
        }
        System.out.println("\n--- Total Revenue ---");
        System.out.println("Total Revenue: $" + totalRevenue);
    }

    private static void printStoreRevenue(Locus locus) {
        System.out.println("\n--- Store Revenue ---");
        int storeCount = 1;
        for (GroceryStore store : locus.getStores()) {
            System.out.println(storeCount + ". " + store.getName());

            Map<GroceryItem, Integer> purchases = store.getPurchases();
            for (Map.Entry<GroceryItem, Integer> entry : purchases.entrySet()) {
                entry.setValue(entry.getValue() / 2); // Divide the quantity by 2
            }
            store.printSummary();
            storeCount++;
        }
    }


    private static void printItemRevenue(Locus locus, Scanner scanner) {
        System.out.println("\n--- Item Revenue ---");

        // Map to store item revenue by item and store
        Map<String, Double> itemRevenueMap = new HashMap<>();

        // Iterate through stores
        for (GroceryStore store : locus.getStores()) {
            // Iterate through purchases in the current store
            for (Map.Entry<GroceryItem, Integer> entry : store.getPurchases().entrySet()) {
                GroceryItem item = entry.getKey();
                int quantity = entry.getValue();

                // Key to identify each item-store combination
                String itemStoreKey = item.getName() + "-" + store.getName();

                // Calculate revenue for the item in the current store
                double revenue = item.getPrice() * quantity;

                // Add revenue to the map, aggregating if the item-store combination already exists
                itemRevenueMap.put(itemStoreKey, itemRevenueMap.getOrDefault(itemStoreKey, 0.0) + revenue);
            }
        }

        // Print unique items from the aggregated revenue map
        int itemCount = 1;
        for (Map.Entry<String, Double> entry : itemRevenueMap.entrySet()) {
            String itemStoreKey = entry.getKey();
            double revenue = entry.getValue();

            // Extract item name and store name from the key
            String[] parts = itemStoreKey.split("-");
            String itemName = parts[0];
            String storeName = parts[1];

            System.out.println(itemCount + ". " + itemName + " from " + storeName);
            itemCount++;
        }

        // Ask user for the item number to view revenue
        System.out.print("Enter the item number to view revenue: ");
        int itemChoice = scanner.nextInt();
        scanner.nextLine();

        // Validate user input and display revenue for the selected item
        if (itemChoice < 1 || itemChoice > itemRevenueMap.size()) {
            System.out.println("Invalid item number.");
        } else {
            // Find the corresponding item-store combination based on the user's choice
            String selectedKey = (String) itemRevenueMap.keySet().toArray()[itemChoice - 1];
            double selectedRevenue = itemRevenueMap.get(selectedKey);

            // Extract item name and store name from the selected key
            String[] parts = selectedKey.split("-");
            String selectedItem = parts[0];
            String selectedStore = parts[1];

            System.out.println("Revenue for " + selectedItem + " from " + selectedStore + ": $" + selectedRevenue);
        }
    }



}