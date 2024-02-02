package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.Feedback;


public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
