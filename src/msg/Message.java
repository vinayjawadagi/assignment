package msg;

import java.util.UUID;

public class Message {
  private String messageId;
  private String content;

  public Message(String content) {
    this.messageId = UUID.randomUUID().toString();
    this.content = content;
  }

  public String toString() {
    return messageId + ": " + content;
  }

}
