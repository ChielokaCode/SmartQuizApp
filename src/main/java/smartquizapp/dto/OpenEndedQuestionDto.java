package smartquizapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class OpenEndedQuestionDto {
    private Long id;
    private String question;
    private String answer;
    private Duration timeLimit;
    private Long points;
    private String subject;

}