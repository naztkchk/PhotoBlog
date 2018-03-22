package com.draxvel.simpleblog.data.source.usersData;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Users implements IUsers{

    @Override
    public void isCurrentUserExists(final IUsers.CurrentUserExistsCallback currentUserExistsCallback) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().exists()){
                        currentUserExistsCallback.onNotExists();
                    }
                }
            }
        });
    }

    @Override
    public void loadCurrentUser(final LoadCurrentUserCallback loadCurrentUserCallback) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(currentUserId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            if(task.getResult().exists()){

                                String name = task.getResult().getString("name");
                                String image = task.getResult().getString("image");

                                loadCurrentUserCallback.OnUploaded(name, image);
                            }else {
                                loadCurrentUserCallback.onFailure("Info about user doesn't exists");
                            }

                        }else{
                            String e = task.getException().getMessage();
                            loadCurrentUserCallback.onFailure(e);
                        }
                    }
                });
    }

    @Override
    public void updateCurrentUser(String imageUrl, String userName, final UpdateCurrentUserCallback updateCurrentUserCallback) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", userName);
        userMap.put("image", imageUrl);

        FirebaseFirestore.getInstance().collection("Users")
                .document(currentUserId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateCurrentUserCallback.onUploaded();
                } else {
                    String e = task.getException().getMessage();
                    updateCurrentUserCallback.onFailure(e);
                }
            }
        });
    }

    @Override
    public void getUserById(String userId, final GetUserByIdCallBack getUserByIdCallBack) {
        FirebaseFirestore.getInstance().collection("Users").
                document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    getUserByIdCallBack.onGet(task.getResult().getString("name"),
                            task.getResult().getString("image"));
                }
            }
        });
    }

    @Override
    public void saveNewProfileImage(Uri mainImageURI, final SaveNewProfileImageCallback saveNewProfileImageCallback) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference imagePath = storageReference.child("profile_images").child(userId + ".jpg");
        imagePath.putFile(mainImageURI)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            saveNewProfileImageCallback.onUploaded(task.getResult().getDownloadUrl().toString());
                        } else {
                            String e = task.getException().getMessage();
                            saveNewProfileImageCallback.onFailure(e);
                        }
                    }
                });
    }


}
