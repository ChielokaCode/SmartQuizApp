package smartquizapp.dto;

import lombok.Data;
import smartquizapp.enums.QuestionType;

import java.util.List;
@Data
public class QuestionDto {

        private String questionContent;
        private Long point;
        private String explanation;

        private QuestionType questionType;
        private List<String> options;
        private List<String> answer;
    }
