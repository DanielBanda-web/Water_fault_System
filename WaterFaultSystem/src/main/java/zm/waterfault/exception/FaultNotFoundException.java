package zm.waterfault.exception;

public class FaultNotFoundException extends RuntimeException {
    public FaultNotFoundException(int id) {
        super("Fault report with ID " + id + " was not found.");
    }
}
