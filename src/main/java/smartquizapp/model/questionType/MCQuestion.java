package smartquizapp.model.questionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import smartquizapp.model.*;


import java.time.Duration;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class MCQuestion extends Question{
@ElementCollection
private List<String> options;

//@ElementCollection
private String answer;


}
