package msg;

import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue implements IMessageQueue {
  private Queue<Message> queue;
  private int capacity;
  private Object addLockObject = new Object(); // lock for adding to the queue
  private Object removeLockObject = new Object(); // lock for removing from the queue

  public MessageQueue(int capacity) {
    this.queue = new LinkedList<>();
    this.capacity = capacity;
  }

  public boolean add(Message item) {
    synchronized (addLockObject) {
      // wait until the queue has space for a new message
      while (queue.size() == capacity) {
        try {
          queue.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      queue.add(item);
      // notify all the threads that the lock on queue is released
      queue.notifyAll();
      return true;
    }
  }

  public Message remove() {
    synchronized (removeLockObject) {
      // wait until the queue has messages to be dequeued
      while (queue.isEmpty()) {
        try {
          queue.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      Message msg = queue.poll();
      // notify all the threads that the lock on the queue is released
      queue.notifyAll();
      return msg;
    }
  }

  public synchronized int size() {
    return queue.size();
  }

  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

}
