package smartquizapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import smartquizapp.model.Subject;
import smartquizapp.serviceImpl.SubjectServiceImpl;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectServiceImpl subjectService;

    @Autowired
    public SubjectController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/fetch-all-subjects")
    public ResponseEntity<List<Subject>> getAllSubject () {
        List<Subject> subjectList =  subjectService.getAllSubject();
        return new ResponseEntity<>(subjectList, HttpStatus.OK);
    }
}
