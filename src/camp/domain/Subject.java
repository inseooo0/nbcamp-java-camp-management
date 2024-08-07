package camp.domain;

public class Subject {
    private String subjectId;
    private String subjectName;
    private SubjectType subjectType;

    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";

    public Subject(String subjectName, SubjectType subjectType) {
        this.subjectId = INDEX_TYPE_SUBJECT + (++subjectIndex);
        this.subjectName = subjectName;
        this.subjectType = subjectType;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public SubjectType getSubjectType() {
        return subjectType;
    }
}
