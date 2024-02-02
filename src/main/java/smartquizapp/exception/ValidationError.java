package smartquizapp.exception;

public record ValidationError(
        String field,
        String message
) {
}