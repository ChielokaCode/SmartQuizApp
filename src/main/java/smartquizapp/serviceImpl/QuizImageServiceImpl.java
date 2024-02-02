package smartquizapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import smartquizapp.model.QuizImage;
import smartquizapp.repository.QuizImageRepository;

import java.util.List;

@Service
public class QuizImageServiceImpl {
    private QuizImageRepository quizImageRepository;
@Autowired
    public QuizImageServiceImpl(QuizImageRepository quizImageRepository) {
        this.quizImageRepository = quizImageRepository;
    }

    public List<QuizImage> allQuizImages() {
        return quizImageRepository.findAll();
    }
}
