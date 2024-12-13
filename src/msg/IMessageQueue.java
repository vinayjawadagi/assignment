package msg;

/**
 * Interface for message queues in the messaging system.
 * Defines operations for a thread-safe queue that handles message
 * storage and retrieval between producers and consumers. Implementations
 * must ensure thread safety for concurrent access.
 */
public interface IMessageQueue {
    /**
     * Adds a message to the queue.
     * If the queue is capacity-constrained and full, this operation
     * should block until space becomes available.
     *
     * @param message The message to add to the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     *         to add the message
     */
    void add(Message message) throws InterruptedException;

    /**
     * Removes and returns the next message from the queue.
     * If the queue is empty, this operation should block until
     * a message becomes available.
     *
     * @return The next message from the queue
     * @throws InterruptedException if the thread is interrupted while waiting
     *         for a message to become available
     */
    Message remove() throws InterruptedException;

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue contains no messages, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the current number of messages in the queue.
     *
     * @return The number of messages currently in the queue
     */
    int size();
}