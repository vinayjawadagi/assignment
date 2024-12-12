package msg;

import java.util.Random;

public class Producer implements Runnable, IProducer {
    private final IMessageQueue messageQueue;
    private final int messageCount;
    private final Random random;

    public Producer(IMessageQueue messageQueue, int messageCount) {
        this.messageQueue = messageQueue;
        this.messageCount = messageCount;
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < messageCount; i++) {
                Message message = generateMessage();
                messageQueue.add(message);
                // Small delay between messages to simulate real world scenario
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Private method to generate a random string with random length
    private Message generateMessage() {
        int length = random.nextInt(100) + 1;
        StringBuilder content = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // generate a random ascii char between 97 to 122 and convert to char
            content.append((char) (random.nextInt(26) + 'a'));
        }
        return new Message(content.toString());
    }
}
