package msg;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for ProgressMonitor class that handles displaying message statistics
 */
class ProgressMonitorTest {
  private MessageStats stats;
  private ByteArrayOutputStream outputStream;
  private PrintStream originalOut;

  @BeforeEach
  void setUp() {
    stats = new MessageStats();
    outputStream = new ByteArrayOutputStream();
    originalOut = System.out;
    System.setOut(new PrintStream(outputStream));
  }

  /**
   * Tests constructor validation for null stats
   */
  @Test
  void testNullStats() {
    assertThrows(IllegalArgumentException.class, () ->
        new ProgressMonitor(null, 1));
  }

  /**
   * Tests constructor validation for negative update interval
   */
  @Test
  void testNegativeInterval() {
     assertThrows(IllegalArgumentException.class, () ->
        new ProgressMonitor(stats, -1));

  }

  /**
   * Tests valid constructor arguments
   */
  @Test
  void testValidArguments() {
    assertDoesNotThrow(() -> new ProgressMonitor(stats, 1));
  }

  /**
   * Tests monitor output format and content
   */
  @Test
  void testStatsOutput() throws InterruptedException {
    stats.incrementSent();
    stats.incrementFailed();
    stats.addProcessingTime(100);

    ProgressMonitor monitor = new ProgressMonitor(stats, 1);
    Thread monitorThread = new Thread(monitor);
    monitorThread.start();
    Thread.sleep(100);
    monitor.stop();
    monitorThread.join();

    String output = outputStream.toString();
    assertTrue(output.contains("SMS Simulation Statistics:"));
    assertTrue(output.contains("Messages Sent: 1"));
    assertTrue(output.contains("Messages Failed: 1"));
    assertTrue(output.contains("Total Processing Time: 100"));
  }

  /**
   * Tests monitor stop functionality
   */
  @Test
  void testMonitorStop() throws InterruptedException {
    ProgressMonitor monitor = new ProgressMonitor(stats, 1);
    Thread monitorThread = new Thread(monitor);
    monitorThread.start();
    monitor.stop();
    monitorThread.join();
    assertFalse(monitorThread.isAlive());
  }

  /**
   * Tests update interval timing
   */
  @Test
  void testUpdateInterval() throws InterruptedException {
    ProgressMonitor monitor = new ProgressMonitor(stats, 1);
    Thread monitorThread = new Thread(monitor);

    long startTime = System.currentTimeMillis();
    monitorThread.start();
    Thread.sleep(1500);
    monitor.stop();
    monitorThread.join();

    String output = outputStream.toString();
    int outputCount = output.split("SMS Simulation Statistics:").length - 1;
    assertTrue(outputCount >= 1);
  }

  /**
   * Cleanup system output stream after each test
   */
  @org.junit.jupiter.api.AfterEach
  void restoreStream() {
    System.setOut(originalOut);
  }
}
