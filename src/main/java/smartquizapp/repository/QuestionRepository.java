package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import smartquizapp.model.Question;
import smartquizapp.model.Quiz;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Question q WHERE q.quiz = :quiz")
    void deleteByQuiz(@Param("quiz") Quiz quiz);


    List<Question> findAllByQuizId(Long id);
    List<Question> findAllByQuiz(Quiz quiz);
}

