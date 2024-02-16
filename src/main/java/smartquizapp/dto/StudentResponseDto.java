package smartquizapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smartquizapp.model.Question;
import smartquizapp.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private Long id;
    private Question question;
    private String answer;
    private Integer scores;
    private User student;
}