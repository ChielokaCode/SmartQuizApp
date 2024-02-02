package smartquizapp.dto;

import lombok.Data;
import smartquizapp.enums.QuestionType;

import java.util.List;
@Data
public class QuestionRequestDto {
    private String questionContent;
    private QuestionType questionType;
    private Long point;
    private int timeLimit;
    private String explanation;
    private String imageUrl;
    private List<String> options;
    private List<String>CorrectAnswers;
}
