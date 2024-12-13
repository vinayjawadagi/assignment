package msg;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A thread-safe message queue implementation using a blocking queue.
 * Provides blocking operations for adding and removing messages, ensuring
 * thread safety when multiple producers and consumers access the queue
 * concurrently.
 */
public class BlockingMessageQueue implements IMessageQueue {
    private final BlockingQueue<Message> queue;

    /**
     * Constructs a new blocking message queue with the specified capacity.
     * When the queue is full, attempts to add messages will block until
     * space becomes available.
     *
     * @param capacity The maximum number of messages the queue can hold
     */
    public BlockingMessageQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Queue capacity must be positive");
        }
        this.queue = new LinkedBlockingQueue<>(capacity);
    }

    /**
     * Constructs a new unbounded blocking message queue.
     * The queue will grow as needed to accommodate new messages.
     */
    public BlockingMessageQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    /**
     * Adds a message to the queue, blocking if necessary until space becomes available.
     *
     * @param message The message to add to the queue
     * @throws InterruptedException if the thread is interrupted while waiting to add
     *         the message
     */
    public void add(Message message) throws InterruptedException {
        if (message == null) {
            throw new NullPointerException("Cannot add null message to queue");
        }
        queue.put(message);
    }

    /**
     * Removes and returns a message from the queue, blocking if necessary until
     * a message becomes available.
     *
     * @return The next message from the queue
     * @throws InterruptedException if the thread is interrupted while waiting for
     *         a message to become available
     */
    public Message remove() throws InterruptedException {
        return queue.take();

    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no messages, false otherwise
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Returns the current number of messages in the queue.
     *
     * @return The number of messages currently in the queue
     */
    public int size() {
        return queue.size();
    }
}
