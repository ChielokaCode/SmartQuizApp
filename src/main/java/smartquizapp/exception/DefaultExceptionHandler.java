package smartquizapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;

@ControllerAdvice
public class DefaultExceptionHandler {
////////////////////////////NOT FOUND
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiError> handleException(
            TokenNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(QuizSubmittedSuccessfully.class)
    public ResponseEntity<ApiError> handleException(
            QuizSubmittedSuccessfully e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.OK, e.getMessage());
    }
    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<ApiError> handleQuizNotFoundException(
            QuizNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<ApiError> handleQuestionNotFoundException(
            QuestionNotFoundException e,
            HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }
////////////////////UNAUTHORIZED
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleUnauthorizedException(
            BadCredentialsException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.UNAUTHORIZED, e.getMessage());
    }

/////////////BAD REQUEST
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUploadProfileImageException(
            UserNotFoundException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(MailConnectionException.class)
    public ResponseEntity<ApiError> handleUploadProfileImageException(
            MailConnectionException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(StudentResponseBadRequest.class)
    public ResponseEntity<ApiError> handleUploadProfileImageException(
            StudentResponseBadRequest e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(PasswordsDontMatchException.class)
    public ResponseEntity<ApiError> handlePasswordsDontMatchException(
            PasswordsDontMatchException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(EmailIsTakenException.class)
    public ResponseEntity<ApiError> handleEmailFoundInDBException(
            EmailIsTakenException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(
            InvalidTokenException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ApiError> handleUserNotVerifiedException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    //////////////////////////ERROR BUILDER///////////////////////////////////////////

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message) {
        return buildErrorResponse(request, status, message, null);
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message, List<ValidationError> errors) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now(),
                errors
        );
        return new ResponseEntity<>(apiError, status);
    }
///////////////////////////////////////END /
}

