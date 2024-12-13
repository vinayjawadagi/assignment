package msg;

import java.util.ArrayList;
import java.util.List;

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
        System.out.println(getStats());
    }

    private void initializeProducer() {
        Producer producer = new Producer(messageQueue, messageCount);
        producerThread = new Thread(producer);
        producerThread.start();
    }

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

    private void initializeMonitor() {
        ProgressMonitor monitor = new ProgressMonitor(stats, monitorInterval);
        monitorThread = new Thread(monitor);
        monitorThread.start();
    }

    private void waitForCompletion() throws InterruptedException {
        producerThread.join();

        while (stats.getSentCount() + stats.getFailedCount() < messageCount) {
            Thread.sleep(100);
        }
    }

    private void shutdown() {
        senderThreads.forEach(Thread::interrupt);
        monitorThread.interrupt();
    }

    public String getStats() {
        return stats.toString();
    }

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
        if (meanDelay < 0) {
            throw new IllegalArgumentException("meanDelay must be non-negative");
        }
        if (monitorInterval <= 0) {
            throw new IllegalArgumentException("monitorInterval must be positive");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MessageAlertSim sim = new MessageAlertSim(1000, 10, 0.1, 100, 2);
        sim.go();
    }
}