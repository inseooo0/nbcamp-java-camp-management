package camp.service;

import camp.domain.Subject;
import camp.domain.SubjectType;
import camp.repository.SubjectRepository;

import java.util.*;

public class SubjectService {

    private SubjectRepository subjectRepository = new SubjectRepository();
    private Scanner sc = new Scanner(System.in);

    public void printSubjectName(List<Subject> subjectList) {
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
    public void printSubjectId(List<Subject> subjectList) {
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

    // 수강 과목의 이름을 입력받아 입력 받은 과목 객체 리스트를 반환
    public Set<Subject> inputSubject(SubjectType type) {
        Set<Subject> subjectSet;
        String subjectInput;
        String description = type.getDescription();
        int required = type.getRequired();

        while (true) {
            subjectSet = new HashSet<>();
            System.out.println(description + " 최소 " + required + "개 이상 입력해주세요(쉼표로 구분)");

            // 과목 이름 출력
            List<Subject> exampleSubjectList = subjectRepository.findByType(type);
            System.out.print(description + " 목록 : ");
            printSubjectName(exampleSubjectList);

            // 과목 입력
            System.out.print(description + " 입력 : ");
            subjectInput = sc.nextLine();

            // 유효성 검사 (입력 개수 확인)
            if (subjectInput.split(",").length < required) {
                System.out.println(description + "은 최소 " + required + "개 이상 입력해야 합니다. 다시 입력해주세요.");
                continue;
            }

            // 유효성 검사 (과목 이름 확인)
            List<String> subjectNames = Arrays.stream(subjectInput.split(","))
                    .map(s -> s.stripLeading().stripTrailing()).toList(); // 문자열 앞뒤 공백 제거

            boolean flag = false;
            for (String subjectName : subjectNames) {
                Subject subject = subjectRepository.findByName(subjectName);
                if (subject == null) {
                    System.out.println(subjectName + "은(는) 존재하지 않는 과목입니다. 다시 입력해주세요.");
                    flag = true;
                    break;
                }
                if (subject.getSubjectType() != type) {
                    System.out.println(subjectName + "은(는) " + subject.getSubjectType().getDescription() + "입니다. " + description + "을 입력해주세요.");
                    flag = true;
                    break;
                }
                subjectSet.add(subject);
            }

            if (flag) continue;

            // 유효성 검사 (중복된 과목 확인)
            if (subjectSet.size() < required) {
                System.out.println(description + "은 최소 " + required + "개 이상 입력해야 합니다. 다시 입력해주세요.");
                continue;
            }

            break;
        }

        return subjectSet;
    }
}
