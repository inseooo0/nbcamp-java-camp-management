package camp.repository;

import camp.domain.Score;

import java.util.ArrayList;
import java.util.List;

public class ScoreRepository {

    private static List<Score> scoreStore = new ArrayList<>();

    public Score save(Score score) {
        scoreStore.add(score);
        return score;
    }

    public Score findOne(String studentId, String subjectId, int round) {
        return scoreStore.stream()
                .filter(s -> s.getStudent().getStudentId().equals(studentId) &&
                        s.getSubject().getSubjectId().equals(subjectId) &&
                        s.getRound() == round)
                .findFirst().orElse(null);
    }

    public List<Score> find(String studentId, String subjectId) {
        return new ArrayList<>(scoreStore.stream()
                .filter(s -> s.getStudent().getStudentId().equals(studentId) &&
                        s.getSubject().getSubjectId().equals(subjectId))
                .toList());
    }

    public void removeById(String studentId) {
        scoreStore.removeIf(s -> s.getStudent().getStudentId().equals(studentId));
    }
}
