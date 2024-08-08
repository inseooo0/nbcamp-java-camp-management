package camp.service;

import camp.domain.Score;
import camp.domain.Student;
import camp.domain.Subject;
import camp.domain.SubjectType;
import camp.repository.ScoreRepository;

import java.util.List;
import java.util.Scanner;

public class ScoreService {

    private ScoreRepository scoreRepository = new ScoreRepository();
    private Scanner sc = new Scanner(System.in);

    public void removeById(String studentId) {
        scoreRepository.removeById(studentId);
    }

    public void inputAndSaveScore(Student student, Subject subject) {
        // 회차 입력
        int round;
        while (true) {
            System.out.print("시험 회차를 입력해주세요(1 ~ 10): ");
            round = sc.nextInt();

            // 회차 범위 유효성 검사
            if (round < 1 || round > 10) {
                System.out.println("시험 회차의 범위는 1 ~ 10 입니다. 다시 입력해주세요.");
                continue;
            }
            // 중복 검사
            Score score = scoreRepository.findOne(student.getStudentId(), subject.getSubjectId(), round);
            if (score == null) {
                break;
            } else {
                System.out.println("해당 회차의 점수는 이미 등록되어 있습니다. 다시 입력해주세요.");
            }
        }

        // 점수 입력
        int score;
        while (true) {
            System.out.print("시험 점수를 입력해주세요(0 ~ 100): ");
            score = sc.nextInt();

            // 회차 범위 유효성 검사
            if (score < 0 || score > 100) {
                System.out.println("시험 점수의 범위는 0 ~ 100 입니다. 다시 입력해주세요.");
            } else {
                break;
            }
        }

        // 점수 등록
        scoreRepository.save(new Score(student, subject, round, score));
    }

    public void updateScore(Student student, Subject subject) {
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

    public void printScore(Student student, Subject subject) {
        // 과목 회차별 등급 출력
        List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
        for (Score score : scoreList) {
            System.out.println("회차 = " + score.getRound());
            System.out.println("등급 = " + score.getGrade() + "\n");
        }

        System.out.println(student.getStudentName() + " 수강생의 "
                + subject.getSubjectName() + " 과목 등급 조회 성공!");
    }

    // 수강생의 과목별 평균 등급 출력
    public void printAvgScore(Student student) {
        // 수강 중인 과목 목록 불러오기
        List<Subject> subjectList = student.getSubjectList();

        for (Subject subject : subjectList) {
            // 평균 등급 산정
            List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
            if (scoreList.isEmpty()) continue; // 점수가 아예 없는 경우 평균 등급 산정 x
            Character avgGrade = getAvgGrade(subject, scoreList);
            System.out.println(subject.getSubjectName() + " 과목의 평균 등급 : " + avgGrade);
        }

    }

    // 수강생의 필수 과목 평균 등급 출력
    public void printAvgMandatoryScore(Student student) {
        List<Subject> subjectList = student.getSubjectList(SubjectType.MANDATORY);
        System.out.println("\n" + student.getStudentName() + " 수강생의 필수 과목 평균 등급");

        for (Subject subject : subjectList) {
            List<Score> scoreList = scoreRepository.find(student.getStudentId(), subject.getSubjectId());
            if (scoreList.isEmpty()) continue; // 점수가 아예 없는 경우 평균 등급 산정 x
            Character avgGrade = getAvgGrade(subject, scoreList);
            System.out.println(subject.getSubjectName() + " 과목의 평균 등급 : " + avgGrade);
        }
    }

    // 과목 객체와 점수 리스트를 입력받아 평균 등급을 반환
    public Character getAvgGrade(Subject subject, List<Score> scoreList) {
        int scoreSum = scoreList.stream().map(Score::getScore).reduce(0, Integer::sum);
        double avgScore = (double) scoreSum / scoreList.size();
        return Score.decideGrade(subject.getSubjectType(), avgScore);
    }
}
