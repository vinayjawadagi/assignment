package msg;

public class MessageStats {
    private final Integer sentCount = 0;
    private final Integer failedCount = 0;
    private final Long totalProcessingTime = 0;

    public void incrementSent() {
        sentCount++;
    }

    public void incrementFailed() {
        failedCount++;
    }

    public void addProcessingTime(long time) {
        totalProcessingTime += time;
    }

    public int getSentCount() {
        return sentCount;
    }

    public long getTotalProcessingTime() {
        return this.totalProcessingTime;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public double getAverageProcessingTime() {
        int total = sentCount + failedCount;
        return total > 0 ? totalProcessingTime / (double) total : 0;
    }
}
