package msg;

import java.util.UUID;

public class Message {
  private String messageId;
  private String content;
  private final long creationTime;
  private boolean failed;
  private long sentTime;

  public Message(String content) {
    this.messageId = UUID.randomUUID().toString();
    this.content = content;
    // Set the time when the message was 'created' in milliseconds
    this.creationTime = System.currentTimeMillis();
  }

  public String getContent() {
    return this.content;
  }

  public long getCreationTime() {
    return this.creationTime;
  }

  public boolean isFailed() {
    return this.failed;
  }

  public void setFailed(boolean failed) {
    this.failed = failed;
  }

  public long getSentTime() {
    return this.sentTime;
  }

  public void setSentTime(long sentTime) {
    this.sentTime = sentTime;
  }

  public String toString() {
    return messageId + ": " + content;
  }

}
