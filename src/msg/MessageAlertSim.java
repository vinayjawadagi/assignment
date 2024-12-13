package msg;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates a message alerting system with multiple senders processing messages from a queue.
 * The simulation tracks message statistics and monitors progress at specified intervals.
 */
public class MessageAlertSim {
    private final int messageCount;
    private final int senderCount;
    private final double failureRate;
    private final int meanDelay;
    private final int monitorInterval;

    private final IMessageQueue messageQueue;
    private final MessageStats stats;
    private final List<Thread> senderThreads;
    private Thread producerThread;
    private Thread monitorThread;

    /**
     * Constructor for a new message alert simulation with specified parameters.
     *
     * @param messageCount The total number of messages to process in the simulation
     * @param senderCount The number of concurrent sender threads to create
     * @param failureRate The probability (0.0 to 1.0) that a message send will fail
     * @param meanDelay The average delay in milliseconds between message sends
     * @param monitorInterval The interval in seconds for progress monitoring
     * @throws IllegalArgumentException if any parameter values are invalid
     */
    public MessageAlertSim(int messageCount, int senderCount, double failureRate,
            int meanDelay, int monitorInterval) {
        validateArguments(messageCount, senderCount, failureRate, meanDelay, monitorInterval);

        this.messageCount = messageCount;
        this.senderCount = senderCount;
        this.failureRate = failureRate;
        this.meanDelay = meanDelay;
        this.monitorInterval = monitorInterval;
        this.messageQueue = new BlockingMessageQueue(senderCount * 2);
        this.stats = new MessageStats();
        this.senderThreads = new ArrayList<>();
    }

    /**
     * Executes the message alert simulation. Creates and manages producer, sender,
     * and monitor threads until all messages are processed.
     *
     * @throws InterruptedException if any thread is interrupted during execution
     */
    public void go() throws InterruptedException {
        // Create the producer thread
        initializeProducer();

        // Create all the sender threads
        initializeSenders();

        // Initialize the monitor thread
        initializeMonitor();

        // Wait for the producer and senders to finish
        waitForCompletion();

        // Stop all the sender threads and the monitor thread
        shutdown();

        // Print the Final Stats
        System.out.println(getFinalStats());
    }

    /**
     * Helper method that initializes and starts the producer thread that will generate messages
     * for the message queue.
     */
    private void initializeProducer() {
        Producer producer = new Producer(messageQueue, messageCount);
        producerThread = new Thread(producer);
        producerThread.start();
    }

    /**
     * Helper method that creates and starts the specified number of sender threads that will process
     * messages from the queue.
     */
    private void initializeSenders() {
        // Create all the sender threads and store their reference in a list
        for (int i = 0; i < senderCount; i++) {
            Sender sender = new Sender(messageQueue, "Sender-" + i,
                failureRate, meanDelay, stats);
            Thread senderThread = new Thread(sender);
            senderThread.start();
            senderThreads.add(senderThread);
        }
    }

    /**
     * Helper method that initializes and starts the monitor thread that tracks and reports
     * simulation progress at regular intervals.
     */
    private void initializeMonitor() {
        ProgressMonitor monitor = new ProgressMonitor(stats, monitorInterval);
        monitorThread = new Thread(monitor);
        monitorThread.start();
    }

    /**
     * Helper method to wait for the producer thread to complete and ensures all messages
     * have been processed (either sent successfully or failed).
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    private void waitForCompletion() throws InterruptedException {
        producerThread.join();

        while (stats.getSentCount() + stats.getFailedCount() < messageCount) {
            Thread.sleep(100);
        }
    }

    /**
     * Helper method to perform cleanup by stopping all sender threads and the monitor thread.
     */
    private void shutdown() {
        senderThreads.forEach(Thread::interrupt);
        monitorThread.interrupt();
    }

    /**
     * Retrieves the final statistics of the message processing simulation.
     *
     * @return A string representation of the final message simulation statistics
     */
    public String getFinalStats() {
        return stats.toString();
    }

    /**
     * Helper method to validate all the constructor arguments.
     */
    private void validateArguments(int messageCount, int senderCount, double failureRate,
                                   int meanDelay, int monitorInterval) {
        if (messageCount <= 0) {
            throw new IllegalArgumentException("messageCount must be positive");
        }
        if (senderCount <= 0) {
            throw new IllegalArgumentException("senderCount must be positive");
        }
        if (failureRate < 0.0 || failureRate >= 1.0) {
            throw new IllegalArgumentException("failureRate must be between 0.0 and 1.0 excluding 1.0");
        }
        if (meanDelay <= 0) {
            throw new IllegalArgumentException("meanDelay must be non-zero and non-negative");
        }
        if (monitorInterval <= 0) {
            throw new IllegalArgumentException("monitorInterval must be positive");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 5) {
            System.err.println("Usage: make run <messageCount> <senderCount> <failureRate> <meanDelay> <monitorInterval>");
            System.exit(1);
        }

        try {
            int messageCount = Integer.parseInt(args[0]);
            int senderCount = Integer.parseInt(args[1]);
            double failureRate = Double.parseDouble(args[2]);
            int meanDelay = Integer.parseInt(args[3]);
            int monitorInterval = Integer.parseInt(args[4]);

            MessageAlertSim sim = new MessageAlertSim(messageCount, senderCount, failureRate, meanDelay, monitorInterval);
            sim.go();
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format in arguments");
            System.exit(1);
        }
    }
}