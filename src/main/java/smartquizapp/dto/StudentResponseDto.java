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
    private Question question;
    private User student;
    private String response;
}