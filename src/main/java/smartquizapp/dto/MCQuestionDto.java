package smartquizapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class MCQuestionDto {
    private Long id;
    private String question;
    private Long points;
    private Duration timeLimit;
    private String answer;
    private String explanation;
    private String subject;
}