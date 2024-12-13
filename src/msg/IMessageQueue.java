package msg;

public interface IMessageQueue {
    void add(Message message) throws InterruptedException;

    Message remove() throws InterruptedException;

    boolean isEmpty();

    int size();
}