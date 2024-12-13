package msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for MessageAlertSim class focusing on public interface and simulation behavior
 */
class MessageAlertSimTest {

  private PrintStream originalOut;

  @BeforeEach
  void setUp() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outputStream));
  }

  /**
   * Tests valid constructor arguments
   */
  @Test
  void testValidConstructorArguments() {
    assertDoesNotThrow(() -> new MessageAlertSim(100, 5, 0.1, 50, 1));
  }

  /**
   * Tests constructor validation for message count
   */
  @Test
  void testInvalidMessageCount() {
    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(0, 5, 0.1, 50, 1));

    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(-1, 5, 0.1, 50, 1));
  }

  /**
   * Tests constructor validation for sender count
   */
  @Test
  void testInvalidSenderCount() {
    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 0, 0.1, 50, 1));

    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, -1, 0.1, 50, 1));
  }

  /**
   * Tests constructor validation for failure rate
   */
  @Test
  void testInvalidFailureRate() {
    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 5, -0.1, 50, 1));

    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 5, 1.0, 50, 1));
  }

  /**
   * Tests constructor validation for mean delay
   */
  @Test
  void testInvalidMeanDelay() {
     assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 5, 0.1, -1, 1));
  }

  /**
   * Tests constructor validation for monitor interval
   */
  @Test
  void testInvalidMonitorInterval() {
    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 5, 0.1, 50, 0));

    assertThrows(IllegalArgumentException.class,
        () -> new MessageAlertSim(100, 5, 0.1, 50, -1));
  }

  /**
   * Tests complete simulation execution with small message count
   */
  @Test
  void testSimulationExecution() throws InterruptedException {
    MessageAlertSim sim = new MessageAlertSim(10, 2, 0.1, 10, 1);
    sim.go();
    String stats = sim.getFinalStats();
    assertTrue(stats.contains("Final Statistics"));
    assertTrue(stats.contains("Total Messages Sent:"));
  }

  /**
   * Tests simulation with boundary values
   */
  @Test
  void testBoundaryValues() throws InterruptedException {
    MessageAlertSim sim = new MessageAlertSim(1, 1, 0.0, 1, 1);
    sim.go();
    String stats = sim.getFinalStats();
    assertTrue(stats.contains("Total Messages Sent:"));
  }

  /**
   * Tests simulation with high failure rate
   */
  @Test
  void testHighFailureRate() throws InterruptedException {
    MessageAlertSim sim = new MessageAlertSim(10, 2, 0.9, 10, 1);
    sim.go();
    String stats = sim.getFinalStats();
    assertTrue(stats.contains("Messages Failed:"));
  }

  /**
   * Tests stats output format
   */
  @Test
  void testStatsOutput() throws InterruptedException {
    MessageAlertSim sim = new MessageAlertSim(5, 1, 0.1, 10, 1);
    sim.go();
    String stats = sim.getFinalStats();
    assertTrue(stats.matches("(?s).*Final Statistics.*Total Messages Sent:.*Total Messages Failed:.*Average Processing Time:.*"));
  }

  /**
   * Tests simulation completion condition
   */
  @Test
  void testSimulationCompletion() throws InterruptedException {
    int messageCount = 20;
    MessageAlertSim sim = new MessageAlertSim(messageCount, 3, 0.1, 10, 1);
    sim.go();
    String stats = sim.getFinalStats();
    String[] parts = stats.split("\\s+");
    int totalProcessed = 0;
    for (int i = 0; i < parts.length; i++) {
      if (parts[i].equals("Sent:")) totalProcessed += Integer.parseInt(parts[i + 1]);
      if (parts[i].equals("Failed:")) totalProcessed += Integer.parseInt(parts[i + 1]);
    }
    assertEquals(messageCount, totalProcessed);
  }

  @Test
  void testSmallScalePerformance() throws InterruptedException {
    long startTime = System.currentTimeMillis();

    MessageAlertSim sim = new MessageAlertSim(100, 5, 0.1, 10, 1);
    sim.go();

    long executionTime = System.currentTimeMillis() - startTime;
    System.setOut(originalOut);
    System.out.println("Total Execution time: " + executionTime + " ms");
  }

  @Test
  void testLargeScalePerformance() throws InterruptedException {
    long startTime = System.currentTimeMillis();

    MessageAlertSim sim = new MessageAlertSim(10000, 20, 0.1, 5, 1);
    sim.go();

    long executionTime = System.currentTimeMillis() - startTime;
    System.setOut(originalOut);
    System.out.println("Total Execution time: " + executionTime + " ms");
  }

  @Test
  void testHighFailureRatePerformance() throws InterruptedException {
    long startTime = System.currentTimeMillis();

    MessageAlertSim sim = new MessageAlertSim(1000, 10, 0.9, 10, 1);
    sim.go();

    long executionTime = System.currentTimeMillis() - startTime;
    System.setOut(originalOut);
    System.out.println("Total Execution time: " + executionTime + " ms");
  }

  @Test
  void comparePerformanceWithDifferentSenderCounts() throws InterruptedException {
    int messageCount = 1000;
    int[] senderCounts = {1, 5, 10, 20};
    long[] executionTimes = new long[senderCounts.length];

    for (int i = 0; i < senderCounts.length; i++) {
      long startTime = System.currentTimeMillis();
      MessageAlertSim sim = new MessageAlertSim(messageCount, senderCounts[i], 0.1, 10, 1);
      sim.go();
      executionTimes[i] = System.currentTimeMillis() - startTime;
    }

    System.setOut(originalOut);
    for (int i = 0; i < senderCounts.length; i++) {
      System.out.printf("Senders: %d, Time: %dms%n", senderCounts[i], executionTimes[i]);
    }
  }

  @Test
  void testPerformanceWithVaryingDelays() throws InterruptedException {
    int[] delays = {1, 10, 50, 100};
    long[] executionTimes = new long[delays.length];

    for (int i = 0; i < delays.length; i++) {
      long startTime = System.currentTimeMillis();
      MessageAlertSim sim = new MessageAlertSim(1000, 10, 0.1, delays[i], 1);
      sim.go();
      executionTimes[i] = System.currentTimeMillis() - startTime;
    }

    System.setOut(originalOut);
    for (int i = 0; i < delays.length; i++) {
      System.out.printf("Delay: %dms, Time: %dms%n", delays[i], executionTimes[i]);
    }
  }

  /**
   * Cleanup system output stream after each test
   */
  @org.junit.jupiter.api.AfterEach
  void restoreStream() {
    System.setOut(originalOut);
  }

}