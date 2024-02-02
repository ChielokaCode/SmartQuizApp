package smartquizapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class StudentResponseBadRequest extends RuntimeException{
    public StudentResponseBadRequest(String message) {
        super(message);
    }
}
