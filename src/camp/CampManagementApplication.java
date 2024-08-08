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
                case 2 -> studentService.updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> studentService.inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> studentService.inquireAvgGradeBySubject(); //수강생의 과목별 평균 등급 조회
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


}
