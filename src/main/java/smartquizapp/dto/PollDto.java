package smartquizapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class PollDto {
    private Long id;
    private String question;
    private Long points;
    private Duration timeLimit;
    private String explanation;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String subject;

}