package t1.workshop4.SyntheticHumanCoreStarter.exceptions;

public class CommandQueueFullException extends RuntimeException {
  public CommandQueueFullException(String message) {
    super(message);
  }
}
