package smartquizapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.OK)
public class QuizSubmittedSuccessfully extends RuntimeException{
    public QuizSubmittedSuccessfully(String message) {
        super(message);
    }
}
