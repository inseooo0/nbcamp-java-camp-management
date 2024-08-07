package camp.service;

import camp.domain.Status;
import camp.domain.Student;
import camp.domain.Subject;
import camp.domain.SubjectType;
import camp.repository.StudentRepository;

import java.util.*;

public class StudentService {

    private StudentRepository studentRepository = new StudentRepository();
    private Scanner sc = new Scanner(System.in);
    private SubjectService subjectService = new SubjectService(); // Service가 다른 Service를 참조 (순환 참조 주의)

    // 수강생 등록
    public void createStudent() {
        System.out.println("\n수강생을 등록합니다...");

        // 수강생 이름 입력받아 저장
        System.out.print("수강생 이름 입력: ");
        String studentName = sc.next();
        Student student = new Student(studentName);

        // 수강생 상태 입력받아 저장
        Status status;
        while (true) {
            System.out.print("수강생의 상태를 입력해주세요(GREEN, YELLOW, RED) : ");
            String statusString = sc.next();
            sc.nextLine(); // 입력 버퍼 비우기
            try {
                status = Status.valueOf(statusString.toUpperCase());
                break;
            } catch (Exception e) {
                System.out.println("수강생의 상태는 Green, Yellow, Red 중 하나여야 합니다. 다시 입력해주세요.");
            }
        }
        student.setStatus(status);

        // 수강 과목 입력
        Set<Subject> mandatoryList = subjectService.inputSubject(SubjectType.MANDATORY);
        Set<Subject> choiceList = subjectService.inputSubject(SubjectType.CHOICE);

        // 수강 과목 저장
        student.getSubjectList().addAll(mandatoryList);
        student.getSubjectList().addAll(choiceList);

        // 수강생 인스턴스 저장
        Student savedStudent = studentRepository.save(student);

        // 저장한 수강생 정보 출력
        printStudent(savedStudent);
        System.out.println("수강생 등록 성공!\n");
    }

    // 수강생 객체를 입력받아 수강생 정보를 출력
    public void printStudent(Student savedStudent) {
        System.out.println("===================================");
        System.out.println("수강생 이름 : " + savedStudent.getStudentName());
        System.out.println("수강생 ID : " + savedStudent.getStudentId());
        System.out.println("수강생 상태 : " + savedStudent.getStatus().name());
        System.out.print("수강 목록 : ");
        List<Subject> subjectList = savedStudent.getSubjectList();
        subjectService.printSubjectName(subjectList);
        System.out.print("수강 목록 ID : ");
        subjectService.printSubjectId(subjectList);
    }
}
