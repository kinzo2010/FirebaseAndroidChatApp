package kiki.chat.firebase.com.firebasechat.util;


import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

/**
 * class này chứa các hàm để thao tác với storage00
 *
 */

public class FireBaseStorageAccess {

    private final  String avatarDefaultUrl = "https://firebasestorage.googleapis.com/v0/b/fir-chatandroid-a29c8.appspot.com/o/user_avatar_default.png?alt=media&token=ae112139-58b7-42a0-a064-09a74f80ca8b";

    private static  FireBaseStorageAccess fireBaseStorageAccess;

    public static FireBaseStorageAccess getInstance() {

        if(fireBaseStorageAccess == null) {

            fireBaseStorageAccess = new FireBaseStorageAccess();


        }

        return fireBaseStorageAccess;

    }


    public void uploadImage(Uri uri, OnSuccessListener onSuccessListener) {

        String fileName = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("/chat-user-avatar/"+fileName);
        storageReference.putFile(uri).addOnSuccessListener(onSuccessListener);

    }

    public void uploadImage(Uri uri, String ref, OnSuccessListener onSuccessListener) {

        String fileName = UUID.randomUUID().toString();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference(ref);
        storageReference.putFile(uri).addOnSuccessListener(onSuccessListener);

    }

    public String getAvatarDefaultUrl() {
        return avatarDefaultUrl;
    }
}
