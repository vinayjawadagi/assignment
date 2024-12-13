// package msg;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;

// public class ProducerTest {
// private Producer producer;
// private MockMessageQueue messageQueue;

// @BeforeEach
// public void setUp() {
// messageQueue = new MockMessageQueue();
// producer = new Producer(messageQueue, 10);
// }

// @Test
// public void testRun() {
// // Test if the producer adds the correct number of messages to the message
// queue
// producer.run();
// assertEquals(10, messageQueue.size());
// }

// @Test
// public void testGenerateMessage() {
// // Test if the generated message is not null
// Message message = producer.generateMessage();
// assertNotNull(message);

// // Test if the generated message has a non-null content
// assertNotNull(message.getContent());

// // Test if the generated message has a non-null timestamp
// assertNotNull(message.getTimestamp());
// }
// }

// // Mock implementation of IMessageQueue for testing purposes
// class MockMessageQueue implements IMessageQueue {
// private List<Message> messages;

// public MockMessageQueue() {
// messages = new ArrayList<>();
// }

// @Override
// public void add(Message message) throws InterruptedException {

// }

// @Override
// public Message remove() throws InterruptedException {
// return null;
// }

// @Override
// public boolean isEmpty() {
// return false;
// }

// @Override
// public int size() {
// return messages.size();
// }
// }
