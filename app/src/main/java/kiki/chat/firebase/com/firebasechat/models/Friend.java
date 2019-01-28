package kiki.chat.firebase.com.firebasechat.models;

import kiki.chat.firebase.com.firebasechat.util.FriendState;

public class Friend {


    FriendState friendState;
    long timestamp;

    public Friend(FriendState friendState, long timestamp) {
        this.friendState = friendState;
        this.timestamp = timestamp;
    }

    public Friend() {
    }



    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public FriendState getFriendState() {
        return friendState;
    }

    public void setFriendState(FriendState friendState) {
        this.friendState = friendState;
    }
}
