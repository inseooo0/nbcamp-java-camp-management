package camp.service;

import camp.domain.*;
import camp.repository.StudentRepository;

import java.util.*;

public class StudentService {

    private StudentRepository studentRepository = new StudentRepository();
    private Scanner sc = new Scanner(System.in);
    private SubjectService subjectService = new SubjectService(); // Service가 다른 Service를 참조 (순환 참조 주의)
    private ScoreService scoreService = new ScoreService(); // Service가 다른 Service를 참조 (순환 참조 주의)

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
        Set<Subject> mandatoryList = subjectService.inputSubjects(SubjectType.MANDATORY);
        Set<Subject> choiceList = subjectService.inputSubjects(SubjectType.CHOICE);

        // 수강 과목 저장
        student.getSubjectList().addAll(mandatoryList);
        student.getSubjectList().addAll(choiceList);

        // 수강생 인스턴스 저장
        Student savedStudent = studentRepository.save(student);

        // 저장한 수강생 정보 출력
        printStudent(savedStudent);
        System.out.println("수강생 등록 성공!\n");
    }

    // 수강생 전체 목록 조회
    public void inquireStudent() {
        System.out.println("\n수강생 목록을 조회합니다...");
        System.out.println("===================================");
        List<Student> studentList = studentRepository.findAll();
        for (Student student : studentList) {
            System.out.println(student);
        }
        System.out.println("\n수강생 목록 조회 성공!");
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

    // 상태별 수강생 목록 조회
    public void inquireStudentByStatus() {
        System.out.println("\n상태별 수강생 목록을 조회합니다...");
        Status[] statuses = Status.values();
        for (Status status : statuses) {
            System.out.println("\n" + status.name() + " 상태 수강생 목록");
            System.out.println("===================================");
            List<Student> students = studentRepository.findByStatus(status);
            for (Student student : students) {
                System.out.println(student);
            }
        }
        System.out.println("\n상태별 수강생 목록 조회 성공!");
    }

    // 수강생 상태 수정
    public void updateStudentStatus() {
        // 수정할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 Id인 경우 종료
        if (student == null) return;

        // 수강생 상태 입력받아 저장
        Status status;
        while (true) {
            System.out.print("수정할 수강생의 상태를 입력해주세요(GREEN, YELLOW, RED) : ");
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
        System.out.println("수강생 상태 수정 성공!\n");
    }

    // 수강생 삭제
    public void deleteStudent() {
        // 삭제할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 Id인 경우 종료
        if (student == null) return;

        // repository 에서 수강생 삭제
        studentRepository.removeById(student.getStudentId());

        // 수강생의 점수 삭제
        scoreService.removeById(student.getStudentId());

        System.out.println("수강생 삭제 완료");
    }

    // 수강생의 과목별 시험 회차 및 점수 등록
    public void createScore() {
        // 관리할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 과목 입력
        Subject subject = subjectService.inputSubject(student);

        // 회차, 점수 입력 및 저장
        scoreService.inputAndSaveScore(student, subject);
    }

    // 수강생의 과목별 회차 점수 수정
    public void updateRoundScoreBySubject() {
        // 관리할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 과목 입력
        Subject subject = subjectService.inputSubject(student);

        // 점수 수정
        scoreService.updateScore(student, subject);
    }

    // 수강생의 특정 과목 회차별 등급 조회
    public void inquireRoundGradeBySubject() {
        // 조회할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 과목 입력
        Subject subject = subjectService.inputSubject(student);

        // 과목 회차별 등급 출력
        scoreService.printScore(student, subject);
    }

    // 수강생의 과목별 평균 등급 조회
    public void inquireAvgGradeBySubject() {
        // 조회할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 수강 중인 과목의 평균 등급 출력
        scoreService.printAvgScore(student);

        System.out.println(student.getStudentName() + " 수강생의 과목별 평균 등급 조회 성공!");
    }

    // 수강생의 ID를 입력받아 수강생 객체를 반환
    public Student getStudent() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        String studentId = sc.next();
        sc.nextLine(); // 버퍼 비우기
        Student student = studentRepository.findById(studentId);

        if (student == null) {
            System.out.println("존재하지 않는 수강생 ID 입니다.");
        }
        return student;
    }
}
