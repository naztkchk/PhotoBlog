package com.draxvel.simpleblog.ui.newpost;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostPresenter implements INewPostPresenter{

    private INewPostView iNewPostView;

    private Uri mainImageURI = null;
    private String currentUserId;

    private StorageReference mStorageRef;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;
    private Bitmap compressedImageFile;


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
            storageReference(desc, mainImageURI);
        }else {
            iNewPostView.showError("Enter info!");
        }
    }

    public void setMainImageURI(Uri mainImageURI) {
        this.mainImageURI = mainImageURI;
    }

    private void storageReference(final String desc, final Uri mainImageURI){
        iNewPostView.setVisibleProgressBar(true);
        final String randomName = UUID.randomUUID().toString();

        StorageReference imagePath = mStorageRef.child("post_images").child(randomName + ".jpg");
        imagePath.putFile(mainImageURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            //upload tumb image
                            UploadTask uploadTask = mStorageRef
                                    .child("post_images/thumbs")
                                    .child(randomName+".jpg")
                                    .putBytes(imageToByteArray(mainImageURI));

                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    storageFirestore(task.getResult().getDownloadUrl().toString()
                                            , taskSnapshot.getDownloadUrl().toString(), desc);

                                }

                                 }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    iNewPostView.showError("uploadTask error: " + e);
                                    iNewPostView.setVisibleProgressBar(false);
                                }
                            });

                            iNewPostView.setVisibleProgressBar(false);

                        } else {
                            String e = task.getException().getMessage();
                            iNewPostView.showError("image error: " + e);
                            iNewPostView.setVisibleProgressBar(false);
                        }
                    }
                });
    }

    private void storageFirestore(final String image_url, final String thumb_url, String desc) {

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("image_url", image_url);
        postMap.put("thumb_url", thumb_url);
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

    private byte[] imageToByteArray(Uri imageURI){
        File imageFile = new File(imageURI.getPath());
        try {
            compressedImageFile = new Compressor(((NewPostActivity)iNewPostView)
                    .getApplicationContext())
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(10)
                    .compressToBitmap(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return  baos.toByteArray();
    }
}
