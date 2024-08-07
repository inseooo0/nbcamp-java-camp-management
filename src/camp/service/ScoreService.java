package camp.service;

import camp.domain.Score;
import camp.domain.Student;
import camp.domain.Subject;
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
}
