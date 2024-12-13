package msg;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks statistics for message processing operations in a thread-safe manner.
 * Maintains counts of successful and failed message sends, as well as processing
 * time metrics. All operations are atomic to ensure thread safety when accessed
 * by multiple sender threads.
 */
public class MessageStats {
    private AtomicInteger sentCount;
    private AtomicInteger failedCount;
    private AtomicLong totalProcessingTime;

    /**
     * Constructs a new MessageStats instance with all counters initialized to zero.
     */
    public MessageStats() {
        this.sentCount = new AtomicInteger(0);
        this.failedCount = new AtomicInteger(0);
        this.totalProcessingTime = new AtomicLong(0);
    }

    /**
     * Atomically increments the count of successfully sent messages.
     */
    public void incrementSent() {
        sentCount.incrementAndGet();
    }

    /**
     * Atomically increments the count of failed message sends.
     */
    public void incrementFailed() {
        failedCount.incrementAndGet();
    }

    /**
     * Atomically adds the specified processing time to the total.
     *
     * @param time The processing time in milliseconds to add
     */
    public void addProcessingTime(long time) {
        if (time < 0) {
            throw new IllegalArgumentException("Processing time cannot be negative");
        }
        totalProcessingTime.addAndGet(time);
    }

    /**
     * Returns the current count of successfully sent messages.
     *
     * @return The number of messages successfully sent
     */
    public int getSentCount() {
        return sentCount.get();
    }

    /**
     * Returns the total processing time for all messages.
     *
     * @return The total processing time in milliseconds
     */
    public long getTotalProcessingTime() {
        return totalProcessingTime.get();
    }

    /**
     * Returns the current count of failed message sends.
     *
     * @return The number of failed message sends
     */
    public int getFailedCount() {
        return failedCount.get();
    }

    /**
     * Calculates the average processing time per message.
     * Includes both successful and failed messages in the calculation.
     *
     * @return The average processing time in milliseconds, or 0 if no messages
     *         have been processed
     */
    public double getAverageProcessingTime() {
        int total = sentCount.get() + failedCount.get();
        return total > 0 ? totalProcessingTime.get() / (double) total : 0;
    }

    /**
     * Returns a formatted string containing the final statistics.
     * Includes total messages sent, failed, and average processing time.
     *
     * @return A formatted string containing all statistics
     */
    public String toString() {
        return "\nFinal Statistics:" +
                "\nTotal Messages Sent: " + getSentCount() +
                "\nTotal Messages Failed: " + getFailedCount() +
                "\nAverage Processing Time: " + getAverageProcessingTime() + " ms";
    }
}
