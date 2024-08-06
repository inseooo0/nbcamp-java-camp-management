package camp.model;


public class Score {
    private String scoreId;
    private Student student;
    private Subject subject;
    private int round;
    private int score;
    private char grade;

    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    public Score(Student student, Subject subject, int round, int score) {
        this.scoreId = INDEX_TYPE_SCORE + (++scoreIndex);
        this.student = student;
        this.subject = subject;
        this.round = round;
        this.score = score;

        this.grade = decideGrade(subject.getSubjectType(), score);
    }

    //getter
    public String getScoreId() {
        return scoreId;
    }

    public Student getStudent() {
        return student;
    }

    public Subject getSubject() {
        return subject;
    }

    public int getRound() {
        return round;
    }

    public int getScore() {
        return score;
    }

    public char getGrade() {
        return grade;
    }

    //setter
    public void setScore(int score) {
        this.score = score;
        grade = decideGrade(subject.getSubjectType(), score);
    }

    public static char decideGrade(SubjectType type, double score) {
        char grade;
        if (type == SubjectType.MANDATORY) {
            if (score >= 95) {
                grade = 'A';
            } else if (score >= 90) {
                grade = 'B';
            } else if (score >= 80) {
                grade = 'C';
            } else if (score >= 70) {
                grade = 'D';
            } else if (score >= 60) {
                grade = 'F';
            } else {
                grade = 'N';
            }
        } else if (type == SubjectType.CHOICE) {
            if (score >= 90) {
                grade = 'A';
            } else if (score >= 80) {
                grade = 'B';
            } else if (score >= 70) {
                grade = 'C';
            } else if (score >= 60) {
                grade = 'D';
            } else if (score >= 50) {
                grade = 'F';
            } else {
                grade = 'N';
            }
        } else {
            throw new IllegalStateException("잘못된 과목 타입입니다. " + type);
        }

        return grade;
    }
}
