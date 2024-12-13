package msg;

/**
 * Interface for message sender components in the messaging system.
 * Defines the base contract for components that process and send messages,
 * typically running in their own thread.
 */
public interface ISender {
  /**
   * Begins message processing operations. This method is typically called
   * when the sender thread starts and should continue processing messages
   * until explicitly stopped.
   */
  void run();
  
  /**
   * Signals the sender to stop processing messages and terminate operations.
   * Implementation should ensure graceful shutdown of any ongoing operations.
   */
  void stop();
}
