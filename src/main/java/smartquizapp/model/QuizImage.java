package smartquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int id;
    @Column(name = "quiz_image")
    private String quizImageFile;

    public QuizImage(String quizImageFile) {
        this.quizImageFile = quizImageFile;
    }
}
