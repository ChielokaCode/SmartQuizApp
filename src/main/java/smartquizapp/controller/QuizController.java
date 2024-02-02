package smartquizapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartquizapp.dto.QuizResponseDto;
import smartquizapp.dto.QuizSubmissionDto;
import smartquizapp.dto.QuizTestDto;
import smartquizapp.dto.SendInviteEmailRequestDto;
import smartquizapp.model.Question;
import smartquizapp.model.QuizImage;
import smartquizapp.serviceImpl.QuizImageServiceImpl;
import smartquizapp.serviceImpl.QuizServiceImpl;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/quiz")
public class QuizController {
    private final QuizImageServiceImpl quizImageService;
    private final QuizServiceImpl quizService;

    @Autowired
    public QuizController(QuizImageServiceImpl quizImageService, QuizServiceImpl quizService) {
        this.quizImageService = quizImageService;
        this.quizService = quizService;
    }

    @GetMapping("/all-quiz-images")
    public ResponseEntity<List<QuizImage>> getAllQuizImages(){
        List<QuizImage> quizImageList = quizImageService.allQuizImages();
        return new ResponseEntity<>(quizImageList, HttpStatus.OK);
    }

    @DeleteMapping("/delete-quiz/{quizId}")
    public ResponseEntity<String> deleteQuiz(@PathVariable("quizId") Long quizId ){
        String response = quizService.deleteQuiz(quizId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PostMapping("/create-quiz")
    public ResponseEntity<String> createQuizQuestion (@Valid @RequestBody QuizTestDto quizTestDto){
        quizService.createQuizQuestion(quizTestDto);
        return ResponseEntity. status(HttpStatus.OK).body("Quiz Created Successfully");
    }

    @GetMapping("/get-quiz/{quizId}")
    public ResponseEntity<QuizResponseDto> getQuizBYEducator(@PathVariable Long quizId) {
        QuizResponseDto quizResponseDTO = quizService.getQuizbyEducator(quizId);
        return ResponseEntity.status(HttpStatus.OK).body(quizResponseDTO);}

    @PutMapping("/edit-quiz/{id}")
    public ResponseEntity<String> editQuizById(@PathVariable Long id, @RequestBody QuizTestDto quizDto) {
        quizService.editQuizById(id, quizDto);
        return ResponseEntity.status(HttpStatus.OK).body("Quiz edited successfully");
    }

    @PostMapping("/send-invite-link")
    public ResponseEntity<String> sendInviteLink(@RequestBody SendInviteEmailRequestDto invite,
                                                 HttpServletRequest request) {
        String response = quizService.sendInviteEmail(invite, request);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<String> publishQuiz(@PathVariable Long id) {
        quizService.publishQuiz(id);
        return ResponseEntity.status(HttpStatus.OK).body("quiz published successfully");
    }

    @GetMapping("/get-published-draft")
    public ResponseEntity<List<QuizResponseDto>> getPublishedDraft(@RequestParam(name = "isPublish", required = false) Boolean isPublish) {
        List<QuizResponseDto> quizResponse = quizService.getAllDraftPublishQuiz(isPublish);
        return ResponseEntity.status(HttpStatus.OK).body(quizResponse);
    }
    @RequestMapping(value = "/take-quiz/{quizId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> takeOrSubmitQuiz(@PathVariable Long quizId, @RequestBody(required = false) QuizSubmissionDto quizSubmission) {
       return new ResponseEntity<>(quizService.takeOrSubmitQuiz(quizId, quizSubmission), HttpStatus.OK);
    }
    @GetMapping("/quizzes-by-subject/{subjectName}")
    public ResponseEntity<List<QuizResponseDto>> getQuizzesBySubject(@PathVariable String subjectName) {
        List<QuizResponseDto> quizResponseDTOs = quizService.getQuizzesBySubject(subjectName);
        return ResponseEntity.status(HttpStatus.OK).body(quizResponseDTOs);
    }
}
