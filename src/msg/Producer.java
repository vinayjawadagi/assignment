package msg;

import java.util.Random;

/**
 * Produces messages and adds them to a message queue for processing.
 * Runs as a separate thread and generates random messages until the specified
 * message count is reached. Each message contains random lowercase alphabetic content
 * of varying length.
 */
public class Producer implements Runnable, IProducer {
    private final IMessageQueue messageQueue;
    private final int messageCount;
    private final Random random;

    /**
     * Constructs a new producer with specified message queue and count.
     *
     * @param messageQueue The queue to which messages will be added
     * @param messageCount The total number of messages to produce
     * @throws IllegalArgumentException if messageCount is not positive
     * @throws NullPointerException if messageQueue is null
     */
    public Producer(IMessageQueue messageQueue, int messageCount) {
        validateArguments(messageQueue, messageCount);
        this.messageQueue = messageQueue;
        this.messageCount = messageCount;
        this.random = new Random();
    }

    /**
     * Executes the message production loop. Generates and adds the specified number
     * of messages to the queue, with a small delay between messages to simulate
     * real-world conditions.
     * If interrupted during execution, the thread will preserve its interrupt status
     * and terminate.
     */
    @Override
    public void run() {
        try {
            for (int i = 0; i < messageCount; i++) {
                Message message = generateMessage();
                messageQueue.add(message);
                // Small delay between messages to simulate sending a message in real world
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Generates a random message with content length between 1 and 100 characters.
     * The content consists of random lowercase letters from 'a' to 'z'.
     *
     * @return A new Message object containing the randomly generated content
     */
    private Message generateMessage() {
        // Get a random message length between 1 and 100
        int length = random.nextInt(100) + 1;
        StringBuilder content = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Generate a random ascii char between 97 and 122 and convert to char
            content.append((char) (random.nextInt(26) + 'a'));
        }

        return new Message(content.toString());
    }

    /**
     * Helper method to validate the constructor arguments.
     */
    private void validateArguments(IMessageQueue messageQueue, int messageCount) {
        if (messageCount <= 0) {
            throw new IllegalArgumentException("Message count must be greater than 0");
        }
        if (messageQueue == null) {
            throw new NullPointerException("Message queue cannot be null");
        }
    }
}
