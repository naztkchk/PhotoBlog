package com.draxvel.simpleblog.data.source.postsData;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Posts implements IPosts{

    public Posts(){}

    @Override
    public void saveImages(final Uri imageUrl, final byte[] image, final SaveImageCallBack saveImagesCallBack) {
        final String randomName = UUID.randomUUID().toString();

        StorageReference imagePath = FirebaseStorage.getInstance().getReference()
                .child("post_images").child(randomName + ".jpg");

        imagePath.putFile(imageUrl)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            UploadTask uploadTask = FirebaseStorage.getInstance().getReference()
                                    .child("post_images/thumbs")
                                    .child(randomName+".jpg")
                                    .putBytes(image);

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    saveImagesCallBack.OnSave(task.getResult().getDownloadUrl().toString(),
                                            taskSnapshot.getDownloadUrl().toString());
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    saveImagesCallBack.OnFailure(e.getMessage().toString());
                                }
                            });

                        } else {
                            String e = task.getException().getMessage();
                            saveImagesCallBack.OnFailure(e);
                        }
                    }
                });
    }

    @Override
    public void publishPost(String image_url, String thumb_url, String desc, final PublishPostCallBack publishPostCallBack) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image_url", image_url);
        postMap.put("thumb_url", thumb_url);
        postMap.put("desc", desc);
        postMap.put("user_id", currentUserId);
        postMap.put("timestamp", FieldValue.serverTimestamp());

        FirebaseFirestore.getInstance().collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    publishPostCallBack.onPublish();
                } else {
                    String e = task.getException().getMessage();
                    publishPostCallBack.onFailure(e);
                }
            }
        });
    }
}
