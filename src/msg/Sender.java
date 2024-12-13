package msg;

import java.util.Random;

public class Sender implements Runnable {

    private final IMessageQueue messageQueue;
    private final String senderId;
    private final double failureRate;
    private final int meanDelay;
    private final Random random;
    private final MessageStats stats;
    private volatile boolean running = true;

    public Sender(
            IMessageQueue messageQueue, String senderId,
            double failureRate, int meanDelay, MessageStats stats) {
        validateArguments(messageQueue, senderId, failureRate, meanDelay, stats);
        this.messageQueue = messageQueue;
        this.senderId = senderId;
        this.failureRate = failureRate;
        this.meanDelay = meanDelay;
        this.random = new Random();
        this.stats = stats;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Message message = messageQueue.remove();

                // Simulate sending, according to the mean generate a random number between 0 to
                // 2 times mean, which gives an average equal to mean
                Thread.sleep(random.nextInt(meanDelay * 2));

                // With the given failure rate set failed to true
                boolean failed = random.nextDouble() < failureRate;
                message.setFailed(failed);
                message.setSentTime(System.currentTimeMillis());

                if (failed) {
                    stats.incrementFailed();
                } else {
                    stats.incrementSent();
                }

                // Add time taken to process the message to total processing time
                stats.addProcessingTime(message.getSentTime() - message.getCreationTime());

            } catch (InterruptedException e) {
                // If Interrupt signal received then stop this thread and set running to false
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    public void stop() {
        running = false;
    }

    private void validateArguments(IMessageQueue messageQueue, String senderId,
                                              double failureRate, int meanDelay, MessageStats stats) {
        if (messageQueue == null) {
            throw new IllegalArgumentException("messageQueue cannot be null");
        }
        if (senderId == null || senderId.trim().isEmpty()) {
            throw new IllegalArgumentException("senderId cannot be null or empty");
        }
        if (failureRate < 0.0 || failureRate >= 1.0) {
            throw new IllegalArgumentException("failureRate must be between 0.0 and 1.0 excluding 1.0");
        }
        if (meanDelay < 0) {
            throw new IllegalArgumentException("meanDelay cannot be negative");
        }
        if (stats == null) {
            throw new IllegalArgumentException("stats cannot be null");
        }
    }
}
