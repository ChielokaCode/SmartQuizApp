package smartquizapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import smartquizapp.dto.QuestionRequestDto;
import smartquizapp.enums.Role;
import smartquizapp.exception.QuestionNotFoundException;
import smartquizapp.exception.UserNotVerifiedException;
import smartquizapp.model.Question;
import smartquizapp.model.User;
import smartquizapp.repository.QuestionRepository;
import smartquizapp.service.QuestionService;

import java.util.Objects;

@Service
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository){
        this.questionRepository=questionRepository;
    }

    public String deleteQuestion(Long questionId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Question question = questionRepository.findById(questionId).orElseThrow(()->new QuestionNotFoundException("Question cannot be found!"));
        if (user.getUserRole().equals(Role.EDUCATOR) && Objects.equals(user.getId(), question.getUser().getId())){
            questionRepository.delete(question);
            return "Question Deleted Successfully";
        }
        throw new UserNotVerifiedException("ONLY EDUCATOR ALLOWED");
    }

    @Override
    public void editQuestion(Long id, QuestionRequestDto questionRequestDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Question question = questionRepository.findById(id).orElseThrow(() -> new RuntimeException("Question not found"));
        if (!Objects.equals(user.getId(), question.getQuiz().getUser().getId())) {
            throw new UserNotVerifiedException("You are not allowed to edit this question");
        }

        if(questionRequestDto.getQuestionContent()!=null && ! questionRequestDto.getQuestionContent().isEmpty()) {
            question.setQuestionContent(questionRequestDto.getQuestionContent());
        }
        if(questionRequestDto.getQuestionType()!=null) {
            question.setQuestionType(questionRequestDto.getQuestionType());
        }
        if(!(questionRequestDto.getPoint()<=0)) {

            question.setPoint(questionRequestDto.getPoint());
        }
        if(questionRequestDto.getExplanation()!=null && ! questionRequestDto.getExplanation().isEmpty()) {
            question.setExplanation(questionRequestDto.getExplanation());
        }
        if(questionRequestDto.getOptions()!=null && ! questionRequestDto.getOptions().isEmpty()) {
            question.setOptions(questionRequestDto.getOptions());
        }
        if(questionRequestDto.getCorrectAnswers()!=null && ! questionRequestDto.getCorrectAnswers().isEmpty()) {

            question.setAnswer(questionRequestDto.getCorrectAnswers());
        }
        questionRepository.save(question);
    }
}