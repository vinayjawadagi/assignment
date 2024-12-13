package msg;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class MessageAlertSimScenarioRuns {

  private MessageAlertSim sim;
  private static StringBuilder results = new StringBuilder();

  /**
   * This scenario runs the simulation for extreme difference in
   * number of Messages to be processed and number of senders.
   */
  @Test
  void testMessageSenderRatio() throws InterruptedException {
    // High message count to sender ratio
    sim = new MessageAlertSim(1000, 2, 0.1, 100, 2);
    sim.go();
    results.append("\n(1000, 2, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");

    // Low message count to sender ratio
    sim = new MessageAlertSim(1000, 500, 0.1, 100, 2);
    sim.go();
    results.append("\n(1000, 500, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");
  }

  /**
   * Tests system behavior with extreme failure rates.
   */
  @Test
  void testFailureRates() throws InterruptedException {
    // High failure
    sim = new MessageAlertSim(1000, 10, 0.9, 100, 2);
    sim.go();
    results.append("\n(1000, 10, 0.9, 100, 2)\n").append(sim.getFinalStats()).append("\n");

    // No failures
    sim = new MessageAlertSim(1000, 10, 0.0, 100, 2);
    sim.go();
    results.append("\n(1000, 10, 0.0, 100, 2)\n").append(sim.getFinalStats()).append("\n");
  }

  /**
   * Tests system performance with extreme processing times.
   * Validates queue behavior and system throughput.
   */
  @Test
  void testDelayTimes() throws InterruptedException {
    // Very low delay
    sim = new MessageAlertSim(1000, 10, 0.1, 1, 2);
    sim.go();
    results.append("\n(1000, 10, 0.1, 10, 2)\n").append(sim.getFinalStats()).append("\n");

    // Very high delay
    sim = new MessageAlertSim(1000, 10, 0.1, 5000, 2);
    sim.go();
    results.append("\n(1000, 10, 0.1, 1000, 2)\n").append(sim.getFinalStats()).append("\n");
  }

  /**
   * Tests system stability with minimal resources.
   * Validates system behavior under resource constraints.
   */
  @Test
  void testResourceConstraints() throws InterruptedException {
    // Single sender
    sim = new MessageAlertSim(1000, 1, 0.1, 100, 2);
    sim.go();
    results.append("\n(1000, 1, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");

    // More senders than messages
    sim = new MessageAlertSim(10, 100, 0.1, 100, 2);
    sim.go();
    results.append("\n(10, 100, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");
  }

  /**
   * Tests queue behavior under heavy load conditions.
   */
  @Test
  void testQueueBehavior() throws InterruptedException {
    // Rapid message generation rate vs low processing rate
    sim = new MessageAlertSim(10000, 2, 0.1, 10, 2);
    sim.go();
    results.append("\n(10000, 2, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");

    // Slow message consumption
    sim = new MessageAlertSim(1000, 5, 0.1, 1000, 2);
    sim.go();
    results.append("\n(100, 1000, 0.1, 100, 2)\n").append(sim.getFinalStats()).append("\n");
  }

  /**
   * Runs after All the tests to print the result
   */
  @AfterAll
  static void printScenarioResults() {
    System.out.println("\n\nScenario Run Results"+"\n" + "=".repeat(80));
    // Print the results of the scenario runs
    System.out.println(results.toString());
  }

}
