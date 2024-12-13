package msg;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue implements IMessageQueue {
    private Queue<Message> queue;
    private int capacity;
    private Object lock = new Object();

    public MessageQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public void add(Message item) throws InterruptedException {
        synchronized (lock) {
            // wait until the queue has space for a new message
            while (queue.size() == capacity) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
            queue.add(item);
            // notify all the threads that the lock on queue is released
            lock.notifyAll();
            // return true;
        }
    }

    public Message remove() throws InterruptedException {
        synchronized (lock) {
            // wait until the queue has messages to be dequeued
            while (queue.isEmpty()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }

            Message msg = queue.poll();
            // notify all the threads that the lock on the queue is released
            lock.notifyAll();
            return msg;
        }
    }

    public int size() {
        synchronized (lock) {
            return queue.size();
        }
    }

    public boolean isEmpty() {
        synchronized (lock) {
            return queue.isEmpty();
        }
    }

}
