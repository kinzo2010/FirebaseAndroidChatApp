package kiki.chat.firebase.com.firebasechat.models;

import java.util.UUID;

public class Group {


    private String groupID, name;
    private long timestamp;

    public Group(String name, long timestamp) {
        this.name = name;

        this.groupID = UUID.randomUUID().toString();
        this.timestamp = timestamp;
    }

    public Group() {
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
