package kiki.chat.firebase.com.firebasechat.models;

public class User {

    private String uid, fullName, email;
    private String avatarRef = "default";
    private String avatarUrl = "https://firebasestorage.googleapis.com/v0/b/fir-chatandroid-a29c8.appspot.com/o/user_avatar_default.png?alt=media&token=ae112139-58b7-42a0-a064-09a74f80ca8b";

    public User(String uid, String fullName, String email, String avatarUrl, String avatarRef) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;

        if (avatarUrl.length() > 1 && avatarRef.length() > 1) {
            this.avatarUrl = avatarUrl;
            this.avatarRef = avatarRef;
        }


    }

    public User(String uid, String fullName, String email) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
    }

    public User() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatarRef() {
        return avatarRef;
    }

    public void setAvatarRef(String avatarRef) {
        this.avatarRef = avatarRef;
    }
}
