package smartquizapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import smartquizapp.model.Question;
import smartquizapp.model.StudentResponse;

import java.util.List;

public interface StudentResponseRepository extends JpaRepository<StudentResponse, Long> {
}
