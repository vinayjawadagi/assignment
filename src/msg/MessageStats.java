package msg;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MessageStats {
    private AtomicInteger sentCount;
    private AtomicInteger failedCount;
    private AtomicLong totalProcessingTime;

    public MessageStats() {
        this.sentCount = new AtomicInteger(0);
        this.failedCount = new AtomicInteger(0);
        this.totalProcessingTime = new AtomicLong(0);
    }

    public void incrementSent() {
        sentCount.incrementAndGet();
    }

    public void incrementFailed() {
        failedCount.incrementAndGet();
    }

    public void addProcessingTime(long time) {
        totalProcessingTime.addAndGet(time);
    }

    public int getSentCount() {
        return sentCount.get();
    }

    public long getTotalProcessingTime() {
        return totalProcessingTime.get();
    }

    public int getFailedCount() {
        return failedCount.get();
    }

    public double getAverageProcessingTime() {
        int total = sentCount.get() + failedCount.get();
        return total > 0 ? totalProcessingTime.get() / (double) total : 0;
    }

    public String toString() {
        return "\nFinal Statistics" +
                "\nTotal Messages Sent: " + getSentCount() +
                "\nTotal Messages Failed: " + getFailedCount() +
                "\nAverage Processing Time: " + getAverageProcessingTime() + " ms";
    }
}
