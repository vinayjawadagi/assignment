package msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SenderTest {
  private IMessageQueue messageQueue;
  private MessageStats stats;
  private Sender sender;
  private static final String SENDER_ID = "TEST_SENDER";
  private static final int MEAN_DELAY = 100;
  private static final String VALID_SENDER_ID = "TEST_SENDER";
  private static final double VALID_FAILURE_RATE = 0.1;
  private static final int VALID_MEAN_DELAY = 100;

  @BeforeEach
  void setUp() {
    messageQueue = new BlockingMessageQueue();
    stats = new MessageStats();
  }

  /**
   * Tests that null message queue throws IllegalArgumentException
   */
  @Test
  void testNullMessageQueue() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(null, VALID_SENDER_ID, VALID_FAILURE_RATE, VALID_MEAN_DELAY, stats));
    assertEquals("messageQueue cannot be null", e.getMessage());
  }

  /**
   * Tests that null sender ID throws IllegalArgumentException
   */
  @Test
  void testNullSenderId() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, null, VALID_FAILURE_RATE, VALID_MEAN_DELAY, stats));
    assertEquals("senderId cannot be null or empty", e.getMessage());
  }

  /**
   * Tests that empty sender ID throws IllegalArgumentException
   */
  @Test
  void testEmptySenderId() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, "", VALID_FAILURE_RATE, VALID_MEAN_DELAY, stats));
    assertEquals("senderId cannot be null or empty", e.getMessage());
  }

  /**
   * Tests that whitespace-only sender ID throws IllegalArgumentException
   */
  @Test
  void testWhitespaceSenderId() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, "   ", VALID_FAILURE_RATE, VALID_MEAN_DELAY, stats));
    assertEquals("senderId cannot be null or empty", e.getMessage());
  }

  /**
   * Tests that negative failure rate throws IllegalArgumentException
   */
  @Test
  void testNegativeFailureRate() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, VALID_SENDER_ID, -0.1, VALID_MEAN_DELAY, stats));
    assertEquals("failureRate must be between 0.0 and 1.0 excluding 1.0", e.getMessage());
  }

  /**
   * Tests that failure rate > 1.0 throws IllegalArgumentException
   */
  @Test
  void testExcessiveFailureRate() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, VALID_SENDER_ID, 1.1, VALID_MEAN_DELAY, stats));
    assertEquals("failureRate must be between 0.0 and 1.0 excluding 1.0", e.getMessage());
  }

  /**
   * Tests that negative mean delay throws IllegalArgumentException
   */
  @Test
  void testNegativeMeanDelay() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, VALID_SENDER_ID, VALID_FAILURE_RATE, -1, stats));
    assertEquals("meanDelay cannot be negative", e.getMessage());
  }

  /**
   * Tests that null stats object throws IllegalArgumentException
   */
  @Test
  void testNullStats() {
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        new Sender(messageQueue, VALID_SENDER_ID, VALID_FAILURE_RATE, VALID_MEAN_DELAY, null));
    assertEquals("stats cannot be null", e.getMessage());
  }

  /**
   * Tests that constructor accepts valid arguments without throwing exceptions
   */
  @Test
  void testValidArguments() {
    assertDoesNotThrow(() ->
        new Sender(messageQueue, VALID_SENDER_ID, VALID_FAILURE_RATE, VALID_MEAN_DELAY, stats));
  }

  /**
   * Tests boundary values [0.0 and 1.0) for failure rate
   */
  @Test
  void testBoundaryFailureRates() {
    assertDoesNotThrow(() ->
        new Sender(messageQueue, VALID_SENDER_ID, 0.0, VALID_MEAN_DELAY, stats));
    assertThrows(IllegalArgumentException.class,
        () -> new Sender(messageQueue, VALID_SENDER_ID, 1.0, VALID_MEAN_DELAY, stats));
  }

  /**
   * Tests normal message processing with 0% failure rate
   */
  @Test
  void testSuccessfulMessageProcessing() throws InterruptedException {

    sender = new Sender(messageQueue, SENDER_ID, 0.0, MEAN_DELAY, stats);
    Thread senderThread = new Thread(sender);

    Message testMessage = new Message("test");
    messageQueue.add(testMessage);

    senderThread.start();
    while (stats.getSentCount() + stats.getFailedCount() < 1) {
      Thread.sleep(10);
    }
    senderThread.interrupt();
    assertEquals(1, stats.getSentCount());
    assertEquals(0, stats.getFailedCount());
    assertFalse(testMessage.isFailed());
  }

  /**
   * Tests message processing with 100% failure rate
   */
  @Test
  void testFailedMessageProcessing() throws InterruptedException {
    sender = new Sender(messageQueue, SENDER_ID, 0.99, MEAN_DELAY, stats);
    Thread senderThread = new Thread(sender);

    Message testMessage = new Message("test");
    messageQueue.add(testMessage);

    senderThread.start();
    while (stats.getSentCount() + stats.getFailedCount() < 1) {
      Thread.sleep(10);
    }
    senderThread.interrupt();

    assertEquals(0, stats.getSentCount());
    assertEquals(1, stats.getFailedCount());
    assertTrue(testMessage.isFailed());
  }

  /**
   * Tests processing time calculation
   */
  @Test
  void testProcessingTimeTracking() throws InterruptedException {
    sender = new Sender(messageQueue, SENDER_ID, 0.0, MEAN_DELAY, stats);
    Thread senderThread = new Thread(sender);

    Message testMessage = new Message("test");
    long startTime = System.currentTimeMillis();
    messageQueue.add(testMessage);

    senderThread.start();
    while (stats.getSentCount() + stats.getFailedCount() < 1) {
      Thread.sleep(10);
    }
    senderThread.interrupt();

    assertTrue(stats.getTotalProcessingTime() >= 0);
    assertTrue(testMessage.getSentTime() > startTime);
  }

  /**
   * Tests processing multiple messages with mixed success/failure
   */
  @Test
  void testMixedSuccessFailure() throws InterruptedException {
    sender = new Sender(messageQueue, SENDER_ID, 0.5, 10, stats);
    Thread senderThread = new Thread(sender);

    int messageCount = 100;
    for (int i = 0; i < messageCount; i++) {
      messageQueue.add(new Message("test" + i));
    }

    senderThread.start();
    while (stats.getSentCount() + stats.getFailedCount() < 100) {
      Thread.sleep(10);
    }
    senderThread.interrupt();

    assertTrue(stats.getFailedCount() > 0);
    assertTrue(stats.getSentCount() > 0);
    assertEquals(messageCount, stats.getSentCount() + stats.getFailedCount());
  }
}