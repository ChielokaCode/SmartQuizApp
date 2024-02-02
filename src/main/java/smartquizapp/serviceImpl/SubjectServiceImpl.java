package smartquizapp.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import smartquizapp.model.Subject;
import smartquizapp.repository.SubjectRepository;

import java.util.List;
@Service
public class SubjectServiceImpl {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubject() {
        return subjectRepository.findAll();
    }
}
