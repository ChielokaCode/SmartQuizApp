package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.Quiz;
import smartquizapp.model.Subject;
import smartquizapp.model.User;

import java.util.List;

public interface QuizTestRepository extends JpaRepository<Quiz, Long> {
//    List<Quiz> findByIsPublicTrue();
     Quiz findByIdAndUser(Long quizId, User user);

//    Quiz findByUserId(Long id);
    List<Quiz> findAllByIsPublishAndUser(Boolean isPublish,User user);


    List<Quiz> findAllBySubjectType(Subject subject);

    Quiz findByUserIdAndId(Long id, Long quizId);
}