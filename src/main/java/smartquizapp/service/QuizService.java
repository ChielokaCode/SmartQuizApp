package smartquizapp.service;


import org.springframework.security.core.Authentication;
import smartquizapp.dto.*;
import smartquizapp.exception.MailConnectionException;


import java.util.List;

public interface QuizService {
    QuizResponseDto getQuiz(Long quizId);
//    void submitQuiz(Long quizId, List<StudentResponseDto> responses);
    void createQuizQuestion(QuizTestDto quizTestDto, Boolean isPublish);
    String deleteQuiz(Long quizId);
    QuizResponseDto getQuizbyEducator(Long quizId);
    void editQuizById(Long id, QuizTestDto quizTestDto);
    String sendInviteEmail(SendInviteEmailRequestDto invite,Long quizId) throws MailConnectionException;
    void publishQuiz(Long quizId);
    public List<QuizResponseDto> getAllDraftPublishQuiz(Boolean isPublish);

//    ResponseEntity<?> takeOrSubmitQuiz(Long quizId, QuizSubmissionDto quizSubmission);

    List<QuizResponseDto> getQuizzesBySubject(String subjectName);
    void submitQuiz(Long quizId, StudentResponseDto studentResponse);
}



