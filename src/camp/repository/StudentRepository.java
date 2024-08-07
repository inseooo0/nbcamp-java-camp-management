package camp.repository;

import camp.domain.Status;
import camp.domain.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {

    private static List<Student> studentStore = new ArrayList<>();

    public Student save(Student student) {
        studentStore.add(student);
        return student;
    }

    public List<Student> findAll() {
        return new ArrayList<>(studentStore);
    }

    public List<Student> findByStatus(Status status) {
        return new ArrayList<>(studentStore.stream()
                .filter(s -> s.getStatus() == status).toList());
    }

    public Student findById(String studentId) {
        return studentStore.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst().orElse(null);
    }

    public void removeById(String studentId) {
        studentStore.removeIf(s -> s.getStudentId().equals(studentId));
    }
}
