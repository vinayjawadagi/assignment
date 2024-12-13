package msg;

/**
 * Monitors and periodically reports statistics for a message processing simulation.
 * This class runs as a separate thread and prints statistics at specified intervals
 * until explicitly stopped or interrupted.
 */
public class ProgressMonitor implements Runnable, IProgressMonitor {
    private final MessageStats stats;
    private final int updateIntervalSeconds;
    private volatile boolean running = true; // define

    /**
     * Constructs a new progress monitor with specified statistics object and update interval.
     *
     * @param stats The message statistics object to monitor
     * @param updateIntervalSeconds The interval in seconds between statistics updates
     * @throws IllegalArgumentException if stats is null or updateIntervalSeconds is negative
     */
    public ProgressMonitor(MessageStats stats, int updateIntervalSeconds) {
        validateArguments(stats, updateIntervalSeconds);
        this.stats = stats;
        this.updateIntervalSeconds = updateIntervalSeconds;
    }

    /**
     * Executes the monitoring loop. Prints statistics at the specified interval
     * until the monitor is stopped or interrupted. This method is called when
     * the monitor thread starts.
     * The loop continues until either:
     * - The stop() method is called, or
     * - The thread receives an interrupt signal
     */
    @Override
    public void run() {
        while (running) {
            try {
                // Print current message stats periodically based on update interval
                printStats();
                Thread.sleep(updateIntervalSeconds * 1000);
            } catch (InterruptedException e) {
                // If Interrupt signal received then stop this thread and set running to false
                Thread.currentThread().interrupt();
                stop();
            }
        }
    }

    /**
     * Helper method that prints the current message processing statistics to the console.
     * Statistics include:
     * - Number of messages successfully sent
     * - Number of failed messages
     * - Total processing time
     * - Average processing time per message
     */
    private void printStats() {
        System.out.println("\nSMS Simulation Statistics:");
        System.out.println("Messages Sent: " + stats.getSentCount());
        System.out.println("Messages Failed: " + stats.getFailedCount());
        System.out.println("Total Processing Time: " + stats.getTotalProcessingTime());
        System.out.printf("Average Processing Time: %f ms\n",
                stats.getAverageProcessingTime());
    }

    /**
     * Stops the monitoring loop. The monitor will complete its current iteration
     * and then terminate.
     */
    public void stop() {
        running = false;
    }

/**
 * Helper method to validate the constructor arguments.
 */
    private void validateArguments(MessageStats stats, int updateIntervalSeconds) {
        if (updateIntervalSeconds < 0) {
            throw new IllegalArgumentException("updateIntervalSeconds must be a positive integer");
        }
        if (stats == null) {
            throw new IllegalArgumentException("stats cannot be null");
        }
    }
}
