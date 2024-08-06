package camp.model;

import java.util.ArrayList;
import java.util.List;

public class SubjectRepository {

    private static List<Subject> subjectStore = List.of(
            new Subject("Java", SubjectType.MANDATORY),
            new Subject("객체지향", SubjectType.MANDATORY),
            new Subject("Spring", SubjectType.MANDATORY),
            new Subject("JPA", SubjectType.MANDATORY),
            new Subject("MySQL", SubjectType.MANDATORY),
            new Subject("디자인 패턴", SubjectType.CHOICE),
            new Subject("Spring Security", SubjectType.CHOICE),
            new Subject("Redis", SubjectType.CHOICE),
            new Subject("MongoDB", SubjectType.CHOICE)
    );

    public List<Subject> findByType(SubjectType type) {
        return new ArrayList<>(subjectStore.stream()
                .filter(s -> s.getSubjectType() == type).toList());
    }

    public Subject findByName(String subjectName) {
        return subjectStore.stream()
                .filter(s -> s.getSubjectName().equalsIgnoreCase(subjectName)) // 대소문자 구분 x
                .findFirst().orElse(null);
    }
}
