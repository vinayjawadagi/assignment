package msg;

/**
 * Interface for message producer components in the messaging system.
 * Defines the contract for components that generate and add messages
 * to a message queue for processing. Producers typically run in their
 * own thread.
 */
public interface IProducer {
    /**
     * Begins message production operations. This method is typically called
     * when the producer thread starts and should continue generating messages
     * until either:
     * - The specified number of messages has been produced
     * - The thread is interrupted
     *
     * Implementations should handle graceful shutdown in case of interruption.
     */
    void run();
}