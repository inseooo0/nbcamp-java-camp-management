package camp.model;

public enum SubjectType {
    MANDATORY("필수 과목", 3), CHOICE("선택 과목", 2);

    private String description;
    private int required;

    SubjectType(String description, int required) {
        this.description = description;
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public int getRequired() {
        return required;
    }
}
