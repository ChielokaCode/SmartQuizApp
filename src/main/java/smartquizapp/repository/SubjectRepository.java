package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Subject findByNameIgnoreCase ( String subjectName);
}