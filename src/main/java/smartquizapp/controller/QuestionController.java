package smartquizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import smartquizapp.dto.QuestionRequestDto;
import smartquizapp.serviceImpl.QuestionServiceImpl;
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/v1/questions")
public class QuestionController {
    private final QuestionServiceImpl questionService;
    @Autowired
    public QuestionController(QuestionServiceImpl questionService){
        this.questionService= questionService;
    }
    @DeleteMapping("/delete-question/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable("questionId") Long questionId){
        String response = questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    @PutMapping("/edit/{questionId}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long questionId, @RequestBody QuestionRequestDto questionRequestDto){
        questionService.editQuestion(questionId,questionRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Question edited successfully");
    }
}
