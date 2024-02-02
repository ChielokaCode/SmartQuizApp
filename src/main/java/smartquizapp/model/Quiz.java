package smartquizapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Subject subjectType;
    private String description;
    private int timeLimit;
    private String topic;
    private String imageUrl;
    private Double totalMarks;
    private Boolean isPrivate;
    private Boolean isPublish;


    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Question> questions;

    @ManyToOne
    private User user;

}
