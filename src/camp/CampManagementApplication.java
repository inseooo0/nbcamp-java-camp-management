package camp;

import camp.domain.*;
import camp.repository.ScoreRepository;
import camp.repository.StudentRepository;
import camp.repository.SubjectRepository;
import camp.service.ScoreService;
import camp.service.StudentService;
import camp.service.SubjectService;

import java.util.*;

public class CampManagementApplication {

    // 스캐너
    private static Scanner sc = new Scanner(System.in);

    // repository
    private static SubjectRepository subjectRepository = new SubjectRepository();
    private static StudentRepository studentRepository = new StudentRepository();
    private static ScoreRepository scoreRepository = new ScoreRepository();

    // service
    private static StudentService studentService = new StudentService();
    private static SubjectService subjectService = new SubjectService();
    private static ScoreService scoreService = new ScoreService();

    public static void main(String[] args) {
        try {
            displayMainView();
        } catch (Exception e) {
            System.out.println("\n오류 발생");
        }
    }

    // 메인 메뉴
    private static void displayMainView() throws InterruptedException {
        boolean flag = true;
        while (flag) {
            System.out.println("\n===================================");
            System.out.println("내일배움캠프 수강생 관리 프로그램 실행 중...");
            System.out.println("1. 수강생 관리");
            System.out.println("2. 점수 관리");
            System.out.println("3. 프로그램 종료");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> displayStudentView(); // 수강생 관리
                case 2 -> displayScoreView(); // 점수 관리
                case 3 -> flag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(2000);
                }
            }
        }
        System.out.println("프로그램을 종료합니다.");
    }

    // 수강생 관리 메뉴
    private static void displayStudentView() {
        boolean flag = true;
        while (flag) {
            System.out.println("===================================");
            System.out.println("수강생 관리 실행 중...");
            System.out.println("1. 수강생 등록");
            System.out.println("2. 수강생 목록 조회 메뉴");
            System.out.println("3. 수강생 상태 수정"); // 수강생 상태 수정
            System.out.println("4. 수강생 삭제"); // 수강생 삭제 추가
            System.out.println("5. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> studentService.createStudent(); // 수강생 등록
                case 2 -> displayStudentInquiry(); // 수강생 목록 조회
                case 3 -> studentService.updateStudentStatus(); // 수강생 상태 수정
                case 4 -> studentService.deleteStudent(); //수강생 삭제
                case 5 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    // 점수 관리 메뉴
    private static void displayScoreView() {
        boolean flag = true;
        while (flag) {
            System.out.println("====================================");
            System.out.println("점수 관리 실행 중...");
            System.out.println("1. 수강생의 과목별 시험 회차 및 점수 등록");
            System.out.println("2. 수강생의 과목별 회차 점수 수정");
            System.out.println("3. 수강생의 특정 과목 회차별 등급 조회");
            System.out.println("4. 수강생의 과목별 평균 등급 조회");
            System.out.println("5. 특정 상태 수강생들의 필수 과목 평균 등급 조회");
            System.out.println("6. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> studentService.createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case 2 -> updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> inquireAvgGradeBySubject(); //수강생의 과목별 평균 등급 조회
                case 5 -> inquireAvgGradeByStatus(); // 특정 상태 수강생들의 필수 과목 평균 등급 조회
                case 6 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    // 수강생 목록 조회 메뉴
    private static void displayStudentInquiry() {
        boolean flag = true;
        while (flag) {
            System.out.println("===================================");
            System.out.println("수강생 목록 조회 메뉴 실행 중...");
            System.out.println("1. 수강생 전체 정보 조회");
            System.out.println("2. 상태별 수강생 조회");
            System.out.println("3. 이전 화면으로 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> studentService.inquireStudent(); // 수강생 전체 목록 조회
                case 2 -> studentService.inquireStudentByStatus(); // 상태별 수강생 목록 조회
                case 3 -> flag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    flag = false;
                }
            }
        }
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        // 관리할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 과목 입력
        Subject subject = inputSubject(student);

        // 회차 입력
        int round;
        Score score;
        while (true) {
            System.out.print("시험 회차를 입력해주세요(1 ~ 10): ");
            round = sc.nextInt();

            // 회차 범위 유효성 검사
            if (round < 1 || round > 10) {
                System.out.println("시험 회차의 범위는 1 ~ 10 입니다. 다시 입력해주세요.");
                continue;
            }
            // 중복 검사
            score = scoreRepository.findOne(student.getStudentId(), subject.getSubjectId(), round);
            if (score != null) {
                break;
            } else {
                System.out.println("해당 회차의 점수는 등록된 기록이 없습니다. 다시 입력해주세요.");
            }
        }

        // 점수 입력
        int newScore;
        while (true) {
            System.out.print("수정할 시험 점수를 입력해주세요(0 ~ 100): ");
            newScore = sc.nextInt();

            // 회차 범위 유효성 검사
            if (newScore < 0 || newScore > 100) {
                System.out.println("시험 점수의 범위는 0 ~ 100 입니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }

        // 점수 수정
        score.setScore(newScore);

        // 수정 사항 출력
        System.out.println("\n시험 점수를 수정합니다...");
        System.out.println(student.getStudentName() + " 수강생의 " + subject.getSubjectName()
                + " 과목 " + round + "회차 점수 " + newScore + "점으로 수정 성공!");
    }

    // 수강생의 특정 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        // 조회할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 과목 입력
        Subject subject = inputSubject(student);

        // 과목 회차별 등급 출력
        List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
        for (Score score : scoreList) {
            System.out.println("회차 = " + score.getRound());
            System.out.println("등급 = " + score.getGrade() + "\n");
        }

        System.out.println(student.getStudentName() + " 수강생의 "
                + subject.getSubjectName() + " 과목 등급 조회 성공!");
    }

    // 수강생의 과목별 평균 등급 조회
    private static void inquireAvgGradeBySubject() {
        // 조회할 수강생 입력
        Student student = getStudent();

        // 존재하지 않는 수강생 Id인 경우 종료
        if (student == null) return;

        // 수강 중인 과목 목록 불러오기
        List<Subject> subjectList = student.getSubjectList();

        for (Subject subject : subjectList) {
            // 평균 등급 산정
            List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
            if (scoreList.isEmpty()) continue; // 점수가 아예 없는 경우 평균 등급 산정 x
            Character avgGrade = getAvgGrade(subject, scoreList);
            System.out.println(subject.getSubjectName() + " 과목의 평균 등급 : " + avgGrade);
        }

        System.out.println(student.getStudentName() + " 수강생의 과목별 평균 등급 조회 성공!");
    }

    // 특정 상태 수강생들의 필수 과목 평균 등급 조회
    private static void inquireAvgGradeByStatus() {
        // 조회할 상태 입력
        Status status;
        while (true) {
            System.out.print("조회할 수강생의 상태를 입력해주세요(GREEN, YELLOW, RED) : ");
            String statusString = sc.next();
            sc.nextLine(); // 입력 버퍼 비우기
            try {
                status = Status.valueOf(statusString.toUpperCase());
                break;
            } catch (Exception e) {
                System.out.println("수강생의 상태는 Green, Yellow, Red 중 하나여야 합니다. 다시 입력해주세요.");
            }
        }

        // 조회할 수강생 목록 불러오기
        List<Student> students = studentRepository.findByStatus(status);

        for (Student student : students) {
            List<Subject> subjectList = student.getSubjectList(SubjectType.MANDATORY);
            System.out.println("\n" + student.getStudentName() + " 수강생의 필수 과목 평균 등급");

            for (Subject subject : subjectList) {
                List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
                if (scoreList.isEmpty()) continue; // 점수가 아예 없는 경우 평균 등급 산정 x
                Character avgGrade = getAvgGrade(subject, scoreList);
                System.out.println(subject.getSubjectName() + " 과목의 평균 등급 : " + avgGrade);
            }
        }

        System.out.println(status.name() + " 상태 수강생들의 필수 과목 평균 등급 조회 성공!");
    }

    // 과목 객체와 점수 리스트를 입력받아 평균 등급을 반환
    private static Character getAvgGrade(Subject subject, List<Score> scoreList) {
        int scoreSum = scoreList.stream().map(Score::getScore).reduce(0, Integer::sum);
        double avgScore = (double) scoreSum / scoreList.size();
        return Score.decideGrade(subject.getSubjectType(), avgScore);
    }

    // 과목 이름을 입력받아 과목 객체 반환
    // 수강 중인 과목 이름을 입력 받을 때까지 반복
    private static Subject inputSubject(Student student) {
        List<Subject> subjectList = student.getSubjectList();
        System.out.print(student.getStudentName() + " 수강생이 수강 중인 과목 : ");
        printSubjectName(subjectList);

        Subject subject;
        while (true) {
            System.out.print("점수를 관리할 과목을 입력해주세요 : ");
            String subjectName = sc.nextLine();

            subject = subjectRepository.findByName(subjectName);

            // 과목 이름 유효성 검사
            if (subject == null) {
                System.out.println(subjectName + "은(는) 존재하지 않는 과목입니다. 다시 입력해주세요.");
                continue;
            }

            // 과목 수강 여부 유효성 검사
            if (!subjectList.contains(subject)) {
                System.out.println(subjectName + "은(는) 수강 중인 과목이 아닙니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }
        return subject;
    }

    // 수강생의 ID를 입력받아 수강생 객체를 반환
    private static Student getStudent() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        String studentId = sc.next();
        sc.nextLine(); // 버퍼 비우기
        Student student = studentRepository.findById(studentId);

        if (student == null) {
            System.out.println("존재하지 않는 수강생 ID 입니다.");
        }
        return student;
    }

    // 과목 리스트를 입력 받아 과목 이름을 출력
    private static void printSubjectName(List<Subject> subjectList) {
        Iterator<Subject> iterator = subjectList.iterator();
        while (iterator.hasNext()) {
            Subject subject = iterator.next();
            System.out.print(subject.getSubjectName());
            if (iterator.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    // 과목 리스트를 입력 받아 과목 ID를 출력
    private static void printSubjectId(List<Subject> subjectList) {
        Iterator<Subject> iterator = subjectList.iterator();
        while (iterator.hasNext()) {
            Subject subject = iterator.next();
            System.out.print(subject.getSubjectId());
            if (iterator.hasNext()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }


}
