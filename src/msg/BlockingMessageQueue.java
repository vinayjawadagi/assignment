package msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingMessageQueue implements IMessageQueue {
    private final BlockingQueue<Message> queue;

    public BlockingMessageQueue(int capacity) {
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    public BlockingMessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public void add(Message message) throws InterruptedException {
        queue.put(message);
    }

    public Message remove() throws InterruptedException {
        return queue.take();

    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
