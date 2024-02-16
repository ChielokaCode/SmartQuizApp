package smartquizapp.dto;

import lombok.Data;
import smartquizapp.enums.QuestionType;

import java.util.List;
@Data
public class QuestionDto {

        private String questionContent;
        private int point;
        private String explanation;

        private QuestionType questionType;
        private List<String> options;
        private String answer;
    }
