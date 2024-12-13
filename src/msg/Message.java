package msg;

import java.util.UUID;

/**
 * Represents a message in the messaging system with associated metadata.
 * Each message has a unique ID, content, timing information, and status flags.
 * Messages track their creation time, sent time, and whether delivery failed.
 */
public class Message {
  private String messageId;
  private String content;
  private final long creationTime;
  private boolean failed;
  private long sentTime;

  /**
   * Constructs a new message with the specified content.
   * Automatically generates a unique message ID and sets the creation timestamp.
   *
   * @param content The text content of the message
   */
  public Message(String content) {
    if (content == null) {
      throw new IllegalArgumentException("Message content cannot be null");
    }
    this.messageId = UUID.randomUUID().toString();
    this.content = content;
    // Set the time when the message was 'created' in milliseconds
    this.creationTime = System.currentTimeMillis();
  }

  /**
   * Returns the text content of the message.
   *
   * @return The message content
   */
  public String getContent() {
    return this.content;
  }

  /**
   * Returns the timestamp when this message was created.
   *
   * @return The creation time in milliseconds since epoch
   */
  public long getCreationTime() {
    return this.creationTime;
  }

  /**
   * Checks if the message delivery failed.
   *
   * @return true if the message failed to deliver, false otherwise
   */
  public boolean isFailed() {
    return this.failed;
  }

  /**
   * Sets the failed status of the message.
   *
   * @param failed true to mark the message as failed, false otherwise
   */
  public void setFailed(boolean failed) {
    this.failed = failed;
  }

  /**
   * Returns the timestamp when this message was sent.
   *
   * @return The sent time in milliseconds since epoch
   */
  public long getSentTime() {
    return this.sentTime;
  }

  /**
   * Sets the timestamp when this message was sent.
   *
   * @param sentTime The sent time in milliseconds since epoch
   */
  public void setSentTime(long sentTime) {
    if (sentTime < this.creationTime) {
      throw new IllegalArgumentException("Sent time cannot be before creation time");
    }
    this.sentTime = sentTime;
  }

  /**
   * Returns a string representation of the message.
   * Format: "{messageId}: {content}"
   *
   * @return A string containing the message ID and content
   */
  public String toString() {
    return messageId + ": " + content;
  }

}
