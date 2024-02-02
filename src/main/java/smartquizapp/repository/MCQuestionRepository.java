package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.questionType.MCQuestion;

public interface MCQuestionRepository extends JpaRepository<MCQuestion,Long> {
}
