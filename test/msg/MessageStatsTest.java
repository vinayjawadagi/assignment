package msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for MessageStats class that tracks message processing statistics
 */
class MessageStatsTest {
  private MessageStats stats;

  @BeforeEach
  void setUp() {
    stats = new MessageStats();
  }

  /**
   * Tests initial state of statistics
   */
  @Test
  void testInitialState() {
    assertEquals(0, stats.getSentCount());
    assertEquals(0, stats.getFailedCount());
    assertEquals(0, stats.getTotalProcessingTime());
    assertEquals(0.0, stats.getAverageProcessingTime());
  }

  /**
   * Tests increment operations
   */
  @Test
  void testIncrements() {
    stats.incrementSent();
    stats.incrementFailed();
    stats.addProcessingTime(100);

    assertEquals(1, stats.getSentCount());
    assertEquals(1, stats.getFailedCount());
    assertEquals(100, stats.getTotalProcessingTime());
    assertEquals(50.0, stats.getAverageProcessingTime());
  }

  /**
   * Tests average processing time calculation
   */
  @Test
  void testAverageProcessingTime() {
    stats.incrementSent();
    stats.incrementSent();
    stats.addProcessingTime(200);

    assertEquals(100.0, stats.getAverageProcessingTime());
  }

  /**
   * Tests average processing time with no messages
   */
  @Test
  void testAverageProcessingTimeWithNoMessages() {
    assertEquals(0.0, stats.getAverageProcessingTime());
  }

  /**
   * Test Illegal addProcessingTime argument
   */
  @Test
  void testIllegalAddProcessingTime() {
    stats.addProcessingTime(-100);
  }
  /**
   * Tests toString output format
   */
  @Test
  void testToString() {
    stats.incrementSent();
    stats.addProcessingTime(100);

    String output = stats.toString();
    assertTrue(output.contains("Final Statistics"));
    assertTrue(output.contains("Total Messages Sent: 1"));
    assertTrue(output.contains("Total Messages Failed: 0"));
    assertTrue(output.contains("Average Processing Time: 100.0 ms"));
  }
}
