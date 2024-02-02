package smartquizapp.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import smartquizapp.dto.QuizResponseDto;
import smartquizapp.dto.QuizSubmissionDto;
import smartquizapp.dto.QuizTestDto;
import smartquizapp.dto.SendInviteEmailRequestDto;
import smartquizapp.model.Question;
import smartquizapp.model.Quiz;

import java.util.List;

public interface QuizService {

    void createQuizQuestion(QuizTestDto quizTestDto);
    String deleteQuiz(Long quizId);
    QuizResponseDto getQuizbyEducator(Long quizId);
    void editQuizById(Long id, QuizTestDto quizTestDto);
    String sendInviteEmail(SendInviteEmailRequestDto invite, HttpServletRequest request);
    void publishQuiz(Long quizId);
    public List<QuizResponseDto> getAllDraftPublishQuiz(Boolean isPublish);

    ResponseEntity<?> takeOrSubmitQuiz(Long quizId, QuizSubmissionDto quizSubmission);

    List<QuizResponseDto> getQuizzesBySubject(String subjectName);
}



