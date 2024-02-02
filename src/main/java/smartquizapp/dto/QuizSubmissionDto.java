package smartquizapp.dto;


import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionDto {
    private Long quizId;
    private List<StudentResponseDto> responses;
}