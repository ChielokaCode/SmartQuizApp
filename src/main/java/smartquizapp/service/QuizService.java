package smartquizapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import smartquizapp.dto.*;
import smartquizapp.model.Question;
import smartquizapp.model.Quiz;

import java.util.List;

public interface QuizService {
    QuizResponseDto getQuiz(Long quizId);
    void submitQuiz(Long quizId, List<StudentResponseDto> responses);
    void createQuizQuestion(QuizTestDto quizTestDto);
    String deleteQuiz(Long quizId);
    QuizResponseDto getQuizbyEducator(Long quizId);
    void editQuizById(Long id, QuizTestDto quizTestDto);
    String sendInviteEmail(SendInviteEmailRequestDto invite,Long quizId, Authentication authentication);
    void publishQuiz(Long quizId);
    public List<QuizResponseDto> getAllDraftPublishQuiz(Boolean isPublish);

    ResponseEntity<?> takeOrSubmitQuiz(Long quizId, QuizSubmissionDto quizSubmission);

    List<QuizResponseDto> getQuizzesBySubject(String subjectName);
}



