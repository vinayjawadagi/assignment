package msg;

import java.util.Random;

/**
 * This class represents a real world entity that 'sends' a message in a message alerting system.
 */
public class Sender implements Runnable, ISender {

    private final IMessageQueue messageQueue;
    private final String senderId;
    private final double failureRate;
    private final int meanDelay;
    private final Random random;
    private final MessageStats stats;
    private volatile boolean running = true;

    /**
     * Constructor for the sender class
     * @param messageQueue Shared message queue instance
     * @param senderId  Generated ID of this sender
     * @param failureRate   Rate with which the message may fail
     * @param meanDelay mean of the delay distribution
     * @param stats Shared instance of the stats
     */
    public Sender(IMessageQueue messageQueue, String senderId,
            double failureRate, int meanDelay, MessageStats stats) {
        validateArguments(messageQueue, senderId, failureRate, meanDelay, stats);
        this.messageQueue = messageQueue;
        this.senderId = senderId;
        this.failureRate = failureRate;
        this.meanDelay = meanDelay;
        this.random = new Random();
        this.stats = stats;
    }

    /**
     * Dequeues message from the shared queue and simulates sending it
     */
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
                stop();
            }
        }
    }

    /**
     * Sets the running flag to false
     */
    public void stop() {
        running = false;
    }

    /**
     * Helper method to validate constructor arguments
     */
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
