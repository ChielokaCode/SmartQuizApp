package smartquizapp.dto;

import lombok.Data;
import smartquizapp.model.Question;

import java.util.List;
@Data
public class QuizTestDto {
    private String subjectType;
    private String description;
    private int timeLimit;
    private String topic;
    private String imageUrl;
    private Double totalMarks;
    private Boolean isPrivate;
    private Boolean isPublish;
    List<Question> questions;
    private List<MCQuestionDto> mcQuestions;
    private List<DragAndDropQuestionDto> dragAndDropQuestions;
    private List<FillBlankQuestionDto> fillBlankQuestions;
    private List<OpenEndedQuestionDto> openEndedQuestions;
    private List<PollDto> polls;
}