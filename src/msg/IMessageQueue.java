package msg;

public interface IMessageQueue {
    boolean add(Message message);

    Message remove();

    boolean isEmpty();

    int size();
}