package smartquizapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class FillBlankQuestionDto {
    private Long id;
    private Long points;
    private String question;
    private Duration timeLimit;

}