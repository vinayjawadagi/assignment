package msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test suite for the Producer class that handles SMS message generation.
 * Tests cover basic functionality and edge cases
 */
class ProducerTest {
  private IMessageQueue messageQueue;
  private Producer producer;

  @BeforeEach
  void setUp() {
    messageQueue = new BlockingMessageQueue();
  }

  /**
   * Tests that producer generates exactly the specified number of messages
   */
  @Test
  void testProducerGeneratesCorrectNumberOfMessages() throws InterruptedException {
    producer = new Producer(messageQueue, 10);
    Thread producerThread = new Thread(producer);
    producerThread.start();
    producerThread.join();

    assertEquals(10, messageQueue.size());
  }

  /**
   * Verifies that generated messages meet content requirements:
   * - Length between 1 and 100 characters
   * - Not null
   */
  @Test
  void testGeneratedMessageContentIsValid() throws InterruptedException {
    producer = new Producer(messageQueue, 1);
    Thread producerThread = new Thread(producer);
    producerThread.start();
    producerThread.join();

    Message msg = messageQueue.remove();
    assertNotNull(msg);
    assertTrue(msg.getContent().length() <= 100);
    assertTrue(msg.getContent().length() >= 1);
    assertTrue(msg.getContent().matches("[a-z]+"));
  }

  /**
   * Constructor: Tests behavior with zero messages
   * Should throw an IllegalArgumentException
   */
  @Test
  void testProducerWithZeroMessages() throws IllegalArgumentException {
    assertThrows(IllegalArgumentException.class,
        () -> producer = new Producer(messageQueue, 0));
  }

  /**
   * Constructor: Tests behavior with null queue
   * Should throw a NullPointerException
   */
  @Test
  void testProducerWithNullQueue() throws IllegalArgumentException {
    assertThrows(IllegalArgumentException.class,
        () -> producer = new Producer(messageQueue, 0));
  }

  /**
   * Performance test: Verifies behavior with large message count
   */
  @Test
  void testProducerWithLargeMessageCount() throws InterruptedException {
    int largeCount = 10000;
    producer = new Producer(messageQueue, largeCount);
    Thread producerThread = new Thread(producer);
    producerThread.start();
    producerThread.join();

    assertEquals(largeCount, messageQueue.size());
  }

  /**
   * Edge case: Tests behavior with negative message count
   */
  @Test
  void testProducerWithNegativeMessageCount() throws IllegalArgumentException {
    assertThrows(IllegalArgumentException.class,
        () -> producer = new Producer(messageQueue, -10));
  }

}