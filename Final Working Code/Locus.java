import java.util.ArrayList;
import java.util.List;

class Locus {
    private List<Person> people;
    private List<Course> courses;
    private List<GroceryStore> stores;

    public Locus() {
        this.people = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.stores = AvailableStores.getStores();
        courses.add(new Course("Math 132- Calculus II"));
        courses.add(new Course("Comp271- Data Structures"));
        courses.add(new Course("Phys 101- Physics I"));
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void registerStudent(Student student, Course course) {
        student.addCourse(course);
        course.addStudent(student);
    }

    public void dropStudent(Student student, Course course) {
        student.removeCourse(course);
        course.removeStudent(student);
    }
    public void processGroceryList(Shopper shopper, GroceryStore store) {
        GroceryList list = shopper.getGroceryList();
        list.printContents();
        store.processList(list);
    }

    public List<GroceryStore> getStores() {
        return stores;
    }
}