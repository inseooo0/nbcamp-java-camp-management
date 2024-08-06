package camp.model;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId; // 수강생 ID
    private String studentName; // 수강생 이름
    private List<Subject> subjectList;
    private Status status;

    private static int studentIndex;
    private static final String INDEX_TYPE_STUDENT = "ST";

    public Student(String studentName) {
        this.studentId = INDEX_TYPE_STUDENT + (++studentIndex);
        this.studentName = studentName;
        this.subjectList = new ArrayList<>();
    }

    //getter
    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public List<Subject> getSubjectList(SubjectType type) {
        return new ArrayList<>(subjectList.stream()
                .filter(s -> s.getSubjectType() == type)
                .toList());
    }

    public Status getStatus() {
        return status;
    }

    public static int getStudentIndex() {
        return studentIndex;
    }

    //setter
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "수강생 Id : " + studentId + ", 수강생 이름 : " + studentName;
    }
}
