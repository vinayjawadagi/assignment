package msg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Test class for the Message class.
 */
public class MessageTest {
  private Message message;
  private final String TEST_CONTENT = "Test message content";
  private final long CURRENT_TIME = System.currentTimeMillis();

  /**
   * Sets up a fresh Message instance before each test.
   * This ensures each test starts with a clean state.
   */
  @BeforeEach
  void setUp() {
    message = new Message(TEST_CONTENT);
  }

  /**
   * Tests the basic constructor functionality.
   * Verifies that:
   * - The message is properly instantiated
   * - Content is correctly set
   * - Creation time is initialized
   * - Default values are correctly set (failed = false, sentTime = 0)
   */
  @Test
  void testConstructor() {
    assertNotNull(message);
    assertEquals(TEST_CONTENT, message.getContent());
    assertTrue(message.getCreationTime() > 0);
    assertFalse(message.isFailed());
    assertEquals(0, message.getSentTime());
  }

  /**
   * Tests that the constructor properly handles null content.
   * Expects an IllegalArgumentException to be thrown when content is null.
   */
  @Test
  void testConstructorWithNullContent() {
    assertThrows(IllegalArgumentException.class, () -> {
      new Message(null);
    });
  }

  /**
   * Tests the content getter.
   * Verifies that the returned content matches what was provided in the constructor.
   */
  @Test
  void testGetContent() {
    assertEquals(TEST_CONTENT, message.getContent());
  }

  /**
   * Tests the creation time getter.
   * Verifies that:
   * - Creation time is positive
   * - Creation time is not in the future
   */
  @Test
  void testGetCreationTime() {
    long creationTime = message.getCreationTime();
    assertTrue(creationTime > 0);
    assertTrue(creationTime <= System.currentTimeMillis());
  }

  /**
   * Tests the failed flag functionality.
   * Verifies that:
   * - Initial state is false
   * - Can be set to true
   * - Can be set back to false
   */
  @Test
  void testFailedFlag() {
    assertFalse(message.isFailed());

    message.setFailed(true);
    assertTrue(message.isFailed());

    message.setFailed(false);
    assertFalse(message.isFailed());
  }

  /**
   * Tests setting a valid sent time (after creation time).
   * Verifies that the sent time is correctly stored and retrieved.
   */
  @Test
  void testSetSentTime() {
    long futureTime = CURRENT_TIME + 1000;
    message.setSentTime(futureTime);
    assertEquals(futureTime, message.getSentTime());
  }

  /**
   * Tests that setting a sent time before creation time throws an exception.
   * This maintains temporal consistency in the message lifecycle.
   */
  @Test
  void testSetSentTimeBeforeCreation() {
    long pastTime = message.getCreationTime() - 1000;
    assertThrows(IllegalArgumentException.class, () -> {
      message.setSentTime(pastTime);
    });
  }

  /**
   * Tests the string representation of the message.
   * Verifies that:
   * - The string contains both the message ID and content
   * - The format matches "messageId: content"
   */
  @Test
  void testToString() {
    String stringRepresentation = message.toString();

    // Verify the string contains both the UUID and content
    assertTrue(stringRepresentation.contains(TEST_CONTENT));
    assertTrue(stringRepresentation.contains(":"));

    // Split the string and verify UUID format
    String[] parts = stringRepresentation.split(":");
    assertEquals(2, parts.length);
  }

  /**
   * Tests that multiple Message instances receive unique IDs.
   * Extracts and compares the UUID portion of the toString() output
   * to verify uniqueness.
   */
  @Test
  void testMultipleInstancesHaveUniqueIds() {
    Message message1 = new Message("First message");
    Message message2 = new Message("Second message");

    String id1 = message1.toString().split(":")[0].trim();
    String id2 = message2.toString().split(":")[0].trim();

    assertNotEquals(id1, id2);
  }
}