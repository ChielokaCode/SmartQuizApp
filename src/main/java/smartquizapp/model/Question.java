package smartquizapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smartquizapp.enums.QuestionType;


import java.time.Duration;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Question content cannot be blank")
    private String questionContent;

    @NotNull(message = "Points cannot be null")
    private int point;

    private int timeLimit;

    private String explanation;

    private String imageUrl;

    private QuestionType questionType;

    @ElementCollection
    private List<String> options;

    private String answer;

    @ManyToOne
    private User user;


    @ManyToOne
    private Quiz quiz;



}
