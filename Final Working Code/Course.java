import java.util.ArrayList;
import java.util.List;

class Course {
    private String name;
    private List<Student> students;
    private List<Student> waitlist;

    public Course(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.waitlist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Student> getWaitlist() {
        return waitlist;
    }

    public void addStudent(Student student) {
        if (students.size() < 10) { // Using a maximum of 10 students per course
            students.add(student);
        } else {
            waitlist.add(student);
            System.out.println(student.getFullName() + " has been added to the waitlist for " + name);
        }
    }

    public void removeStudent(Student student) {
        if (students.remove(student)) {
            if (!waitlist.isEmpty()) {
                Student waitlistedStudent = waitlist.remove(0);
                students.add(waitlistedStudent);
                System.out.println(waitlistedStudent.getFullName() + " has been moved from the waitlist to the registered students for " + name);
            }
        }
    }

    public void printStudents() {
        System.out.println("\nRegistered Students for " + name + ":");
        if (students.isEmpty()) {
            System.out.println("No students registered.");
        } else {
            for (Student student : students) {
                System.out.println("- " + student.getFullName());
            }
        }
    }

    public void printWaitlist() {
        System.out.println("\nWaitlisted Students for " + name + ":");
        if (waitlist.isEmpty()) {
            System.out.println("No students on the waitlist.");
        } else {
            for (Student student : waitlist) {
                System.out.println("- " + student.getFullName());
            }
        }
    }
}