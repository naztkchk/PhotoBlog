package com.draxvel.simpleblog.newpost;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
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
import java.util.Random;

public class NewPostPresenter implements INewPostPresenter{

    private INewPostView iNewPostView;

    private Uri mainImageURI = null;
    private String currentUserId;
    private static final int MAX_LENGTH = 100;

    private StorageReference mStorageRef;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;


    public NewPostPresenter(INewPostView iNewPostView) {
        this.iNewPostView = iNewPostView;

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void publishPost(final String desc) {
        if (!TextUtils.isEmpty(desc) && mainImageURI != null) {

            iNewPostView.setVisibleProgressBar(true);

            String randomName = random();

            StorageReference imagePath = mStorageRef.child("post_images").child(randomName + ".jpg");
            imagePath.putFile(mainImageURI)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageFirestore(task, desc);

                                } else {
                                    String e = task.getException().getMessage();
                                    iNewPostView.showError("image error: " + e);
                                    iNewPostView.setVisibleProgressBar(false);
                                }
                            }
                        });
        }else {
            iNewPostView.showError("Enter info!");
        }
    }

    public void setMainImageURI(Uri mainImageURI) {
        this.mainImageURI = mainImageURI;
    }

    private void storageFirestore(Task<UploadTask.TaskSnapshot> task, String desc) {

        String download_uri = task.getResult().getDownloadUrl().toString();

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image_url", download_uri);
        postMap.put("desc", desc);
        postMap.put("user_id", currentUserId);
        postMap.put("timestamp", FieldValue.serverTimestamp());

        mFirebaseFirestore.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {

                    iNewPostView.showError("The post is published");
                    iNewPostView.showMainActivity();

                } else {
                    String e = task.getException().getMessage();
                    iNewPostView.showError("firestore error: " + e);
                }

                iNewPostView.setVisibleProgressBar(false);
            }
        });
    }

    private static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
