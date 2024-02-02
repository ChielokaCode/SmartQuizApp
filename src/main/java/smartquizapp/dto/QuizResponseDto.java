package smartquizapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smartquizapp.model.Subject;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponseDto {
        private Subject subjectType;
        private String description;
        private int timeLimit;
        private String topic;
        private String imageUrl;
        private Double totalMarks;
        private Boolean isPrivate;
        private Boolean isPublish;
        private List<QuestionDto> questions;


    }

