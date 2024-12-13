package msg;

public class ProgressMonitor implements Runnable {
    private final MessageStats stats;
    private final int updateIntervalSeconds;
    private volatile boolean running = true; // define

    public ProgressMonitor(MessageStats stats, int updateIntervalSeconds) {
        validateArguments(stats, updateIntervalSeconds);
        this.stats = stats;
        this.updateIntervalSeconds = updateIntervalSeconds;
    }

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
                running = false;
            }
        }
    }

    // Helper method to print stats out to console
    private void printStats() {
        System.out.println("\nSMS Simulation Statistics:");
        System.out.println("Messages Sent: " + stats.getSentCount());
        System.out.println("Messages Failed: " + stats.getFailedCount());
        System.out.println("Total Processing Time: " + stats.getTotalProcessingTime());
        System.out.printf("Average Processing Time: %f ms\n",
                stats.getAverageProcessingTime());
    }

    public void stop() {
        running = false;
    }

    private void validateArguments(MessageStats stats, int updateIntervalSeconds) {
        if (updateIntervalSeconds < 0) {
            throw new IllegalArgumentException("updateIntervalSeconds must be a positive integer");
        }
        if (stats == null) {
            throw new IllegalArgumentException("stats cannot be null");
        }
    }
}
