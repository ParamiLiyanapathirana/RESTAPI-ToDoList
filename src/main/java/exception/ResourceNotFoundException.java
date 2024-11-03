package exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);  // Call the superclass constructor with the message
    }
}
