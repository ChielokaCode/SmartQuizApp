package smartquizapp.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;

@Data
@Builder
public class DragAndDropQuestionDto {
    private Long id;
    private String dragItems;
    private String question;
    private Long points;
    private byte[] image;
    private String dragTarget;
    private String correctMapping;
    private Duration timeLimit;
}
