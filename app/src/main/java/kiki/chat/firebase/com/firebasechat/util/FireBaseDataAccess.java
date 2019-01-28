package kiki.chat.firebase.com.firebasechat.util;


import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import kiki.chat.firebase.com.firebasechat.models.ChatMessage;
import kiki.chat.firebase.com.firebasechat.models.Friend;
import kiki.chat.firebase.com.firebasechat.models.Group;
import kiki.chat.firebase.com.firebasechat.models.GroupChatMessage;
import kiki.chat.firebase.com.firebasechat.models.LatestMessage;
import kiki.chat.firebase.com.firebasechat.models.User;

/**
 * class này chứa các hàm để thao tác với database
 */

public class FireBaseDataAccess {


    private static FireBaseDataAccess fireBaseDataAccess;
    String myUid = FirebaseAuth.getInstance().getUid();

    public static FireBaseDataAccess getInstance() {

        if (fireBaseDataAccess == null) {
            fireBaseDataAccess = new FireBaseDataAccess();
        }

        return fireBaseDataAccess;

    }

    /**
     * Lưu thông tin của user lên database
     *
     * @param user Thông tin của user
     * @param uri  đường dẫn đến file ảnh trong bộ nhớ của thiết bị, trong trường hợp người dùng muốn lưu ảnh đại diện
     */

    public void saveUser(final User user, Uri uri, final OnSuccessListener onSuccessListener) {

        if (uri != null) {

            //upload avatar
            FireBaseStorageAccess.getInstance().uploadImage(uri, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("upload image", "success --> " + taskSnapshot.getMetadata().getPath());
                    user.setAvatarRef(taskSnapshot.getMetadata().getPath());

                    //upload thành công, get link download avatar
                    FirebaseStorage.getInstance().getReference(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uriDownload) {

                            //get link download thành công
                            Log.d("upload image", "url download --> " + uriDownload.toString());
                            user.setAvatarUrl(uriDownload.toString());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
                            databaseReference.setValue(user).addOnSuccessListener(onSuccessListener);
                        }
                    });


                }

            });

        } else {

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
            databaseReference.setValue(user).addOnSuccessListener(onSuccessListener);
        }
    }

    /**
     * update profile khi user update cả thông tin cá nhân và avatar
     *
     * @param user              chứa thông tin mới của user
     * @param uri               đường dẫn đến file ảnh trong bộ nhớ máy
     * @param onSuccessListener hàm này được gọi sau khi update hoàn tất
     */

    public void upadeUserProfile(final User user, Uri uri, final OnSuccessListener onSuccessListener) {
        /**
         *
         * khi avatar đang là mặc định thì trường avatarRef của user có giá trị là "default"
         * lúc này sẽ upload avatar mới
         */

        if (user.getAvatarRef().length() > 0 && user.getAvatarRef().equals("default")) {





            if(uri != null) {

                FireBaseStorageAccess.getInstance().uploadImage(uri, new OnSuccessListener<UploadTask.TaskSnapshot>() {


                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        /**
                         * Lưu đường dẫn đến đến file vừa up, phục vụ việc update sau này
                         *
                         */

                        user.setAvatarRef(taskSnapshot.getMetadata().getPath());

                        /**
                         * upload thành công, lấy url của file
                         */

                        FirebaseStorage.getInstance().getReference(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uriDownload) {
                                user.setAvatarUrl(uriDownload.toString());

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
                                databaseReference.setValue(user).addOnSuccessListener(onSuccessListener);

                            }
                        });

                    }
                });

            }


            /**
             *
             * trường hợp này người dùng đã có avatar từ trước, khác với avatar mặc định
             *
             */

        } else {


            FireBaseStorageAccess.getInstance().uploadImage(uri, user.getAvatarRef(), new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    user.setAvatarRef(taskSnapshot.getMetadata().getPath());

                    FirebaseStorage.getInstance().getReference(taskSnapshot.getMetadata().getPath()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uriDownload) {
                            user.setAvatarUrl(uriDownload.toString());

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
                            databaseReference.setValue(user).addOnSuccessListener(onSuccessListener);

                        }
                    });

                }
            });

        }

    }

    /**
     * update thông tin cá nhân khi user chỉ update thông tin cá nhân, KHÔNG update avatar mới
     *
     * @param user
     * @param onSuccessListener
     */

    public void updateUserProfile(User user, OnSuccessListener onSuccessListener) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + user.getUid());
        databaseReference.setValue(user).addOnSuccessListener(onSuccessListener);

    }



    /**
     * Lấy thông tin người dùng
     *
     * @param uid                uid của uesr muốn lây thông tin
     * @param valueEventListener chạy hàm này khi qua trình lưu đữ liệu lên server hoàn tất
     */

    public void getUserProfile(String uid, ValueEventListener valueEventListener) {

        FirebaseDatabase.getInstance().getReference("/users/" + uid).addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * Lây thông tin của người đang đăng nhập
     *
     * @param valueEventListener
     */

    public void getCurrentUserProfile(ValueEventListener valueEventListener) {

        FirebaseDatabase.getInstance().getReference("/users/" + FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * lấy danh sách toàn bộ user trong hệ thống
     *
     * @param valueEventListener
     */

    public void getUserList(ValueEventListener valueEventListener) {

        FirebaseDatabase.getInstance().getReference("/users").addListenerForSingleValueEvent(valueEventListener);
    }

    /**
     * user gửi tin nhắn cho user khác
     *
     * @param chatMessage nội dung của tin nhắn
     * @param toId        uid của người nhận tin nhắn
     */

    public void sendMessageToUser(ChatMessage chatMessage, String toId) {
        String myUid = FirebaseAuth.getInstance().getUid();

        DatabaseReference toRef = FirebaseDatabase.getInstance().getReference("/user-message/" + myUid + "/" + toId).push();
        toRef.setValue(chatMessage);

        DatabaseReference fromRef = FirebaseDatabase.getInstance().getReference("/user-message/" + toId + "/" + myUid).push();
        fromRef.setValue(chatMessage);

        FirebaseDatabase.getInstance().getReference("/latest-message/" + myUid + "/" + toId).setValue(new LatestMessage(toId, myUid, chatMessage.getMessage(), chatMessage.getTimestamp()));
        FirebaseDatabase.getInstance().getReference("/latest-message/" + toId + "/" + myUid).setValue(new LatestMessage(myUid, myUid, chatMessage.getMessage(), chatMessage.getTimestamp()));
    }

    /**
     * gửi yêu cầu kết bạn
     *
     * @param uid uid của người muốn gửi yêu cầu kết bạn
     */

    public void sendFriendRequest(String uid) {
        FirebaseDatabase.getInstance().getReference("/friend/" + myUid + "/" + uid).setValue(new Friend(FriendState.WAIT_FOR_ACCEPT, System.currentTimeMillis() / 1000));
        FirebaseDatabase.getInstance().getReference("/friend/" + uid + "/" + myUid).setValue(new Friend(FriendState.FRIEND_REQUEST, System.currentTimeMillis() / 1000));
    }

    /**
     * save group chat len server
     *
     * @param group
     * @param onSuccessListener
     */

    public void saveGroupChat(Group group, OnSuccessListener onSuccessListener) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/group-chat/" + group.getGroupID());
        databaseReference.setValue(group).addOnSuccessListener(onSuccessListener);

    }

    public void sendMessageToGroup(GroupChatMessage groupChatMessage, String groupID) {
        DatabaseReference toRef = FirebaseDatabase.getInstance().getReference("/group-chat-message/" + groupID ).push();
        toRef.setValue(groupChatMessage);


    }

}
