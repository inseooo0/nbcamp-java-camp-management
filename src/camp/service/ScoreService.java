package camp.service;

import camp.repository.ScoreRepository;

public class ScoreService {

    private ScoreRepository scoreRepository = new ScoreRepository();

    public void removeById(String studentId) {
        scoreRepository.removeById(studentId);
    }
}
