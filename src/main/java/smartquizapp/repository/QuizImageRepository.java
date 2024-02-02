package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.QuizImage;

public interface QuizImageRepository extends JpaRepository<QuizImage, Integer> {
}
