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

                // Simulate sending according to the mean generate a random number between 0 to
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
}
