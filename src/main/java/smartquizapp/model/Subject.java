package smartquizapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageFilename;
    private String backgroundColor;

    public Subject(String name, String description, String imageFilename, String backgroundColor) {
        this.name = name;
        this.description = description;
        this.imageFilename = imageFilename;
        this.backgroundColor = backgroundColor;
    }
}
