package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smartquizapp.model.questionType.DragAndDropQuestion;

@Repository
public interface DragAndDropQuestionRepository extends JpaRepository<DragAndDropQuestion, Long> {

}