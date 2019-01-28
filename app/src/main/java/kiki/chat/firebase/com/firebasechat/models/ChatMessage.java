package kiki.chat.firebase.com.firebasechat.models;

public class ChatMessage {


    private String fromId, message;
    private long timestamp;

    public ChatMessage(String fromId, String message, long timestamp) {

        this.fromId= fromId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatMessage() {
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
