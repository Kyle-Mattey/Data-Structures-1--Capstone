import java.util.ArrayList;
import java.util.List;

class Student extends Person {
    private List<Course> courses;

    public Student(String firstName, String lastName) {
        super(firstName, lastName);
        this.courses = new ArrayList<>();
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
    }
}