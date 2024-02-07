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
    private Long questionId;
    private String response;
    private User student;
}