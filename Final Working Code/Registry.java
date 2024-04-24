import java.util.HashMap;
import java.util.Map;

class Registry {
    private static Map<String, Course> courses = new HashMap<>();

    public static void addCourse(Course course) {
        courses.put(course.getName(), course);
    }

    public static Course getCourse(String name) {
        return courses.get(name);
    }
}