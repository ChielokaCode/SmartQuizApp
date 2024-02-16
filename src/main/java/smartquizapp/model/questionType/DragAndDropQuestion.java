package smartquizapp.model.questionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smartquizapp.model.Answer;
import smartquizapp.model.Options;
import smartquizapp.model.Question;

import java.time.Duration;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DragAndDropQuestion extends Question {

    @ElementCollection
    private List<String> options;

//    @ElementCollection
    private String answer;



}


