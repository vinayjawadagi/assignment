package msg;

/**
 * Interface for monitoring components that track and report progress
 * in the messaging system. Monitors typically run in a separate thread
 * and periodically report system statistics or status updates.
 */
public interface IProgressMonitor {
  /**
   * Begins monitoring operations. This method is typically called when
   * the monitor thread starts and should continue until explicitly stopped.
   * Implementations should periodically collect and report system metrics.
   */
  void run();

  /**
   * Signals the monitor to stop collecting and reporting metrics.
   * Implementation should ensure graceful shutdown of monitoring operations.
   */
  void stop();
}