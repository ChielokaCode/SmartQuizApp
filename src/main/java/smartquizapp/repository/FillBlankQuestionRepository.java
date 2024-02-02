package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.questionType.FillBlankQuestion;

public interface FillBlankQuestionRepository extends JpaRepository<FillBlankQuestion, Long> {
}
