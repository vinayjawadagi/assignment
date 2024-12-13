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
        this.messageCount = messageCount;
        this.senderCount = senderCount;
        this.failureRate = failureRate;
        this.meanDelay = meanDelay;
        this.monitorInterval = monitorInterval;

        // this.messageQueue = new BlockingMessageQueue();
        this.messageQueue = new BlockingMessageQueue(senderCount * 2);
        // this.messageQueue = new MessageQueue(senderCount * 2);
        this.stats = new MessageStats();
        this.senderThreads = new ArrayList<>();
    }

    public void go() throws InterruptedException {

        // Create the producer thread
        Producer producer = new Producer(messageQueue, messageCount);
        producerThread = new Thread(producer);
        producerThread.start();

        // Create all the sender threads and store their reference in a list
        for (int i = 0; i < senderCount; i++) {
            Sender sender = new Sender(messageQueue, "Sender " + i,
                    failureRate, meanDelay, stats);
            Thread senderThread = new Thread(sender);
            senderThread.start();
            senderThreads.add(senderThread);
        }
        // Initialize the monitor thread
        ProgressMonitor monitor = new ProgressMonitor(stats, monitorInterval);
        monitorThread = new Thread(monitor);
        monitorThread.start();

        // Wait for the producer to finish generating all the messages
        producerThread.join();

        // Wait until all the messages ahve been sent, check periodically
        while (stats.getSentCount() + stats.getFailedCount() < messageCount) {
            System.out.println(messageCount);
            System.out.println(stats.getSentCount() + stats.getFailedCount());
            Thread.sleep(100);
        }

        // Stop all the sender threads and the monitor thread
        senderThreads.forEach(Thread::interrupt);
        monitorThread.interrupt();
        System.out.println(stats.toString());
    }

    public static void main(String[] args) throws InterruptedException {
        MessageAlertSim sim = new MessageAlertSim(1000, 10, 0.1, 100, 2);
        sim.go();
    }
}