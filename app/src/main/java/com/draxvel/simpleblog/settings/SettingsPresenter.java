package com.draxvel.simpleblog.settings;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

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
import java.util.Random;

public class SettingsPresenter implements ISettingsPresenter{

    private Uri mainImageURI = null;
    private String userId;
    private boolean isProfileChanged = false;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore mFirebaseFirestore;

    private ISettingsView iSettingsView;

    public SettingsPresenter(ISettingsView iSettingsView){
        this.iSettingsView = iSettingsView;

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        iSettingsView.setEnabledSaveBtn(false);

        loadStoredInfo();
    }

    public void setMainImageURI(Uri mainImageURI) {
        this.mainImageURI = mainImageURI;
    }

    public void setProfileChanged(boolean profileChanged) {
        isProfileChanged = profileChanged;
    }

    private void loadStoredInfo() {
        mFirebaseFirestore
                .collection("Users")
                .document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");

                        mainImageURI = Uri.parse(image);

                        iSettingsView.setName(name);
                        iSettingsView.setImage(image);
                    }

                }else{
                    String e = task.getException().getMessage();
                    iSettingsView.showError("firestore retrieve  error: "+e);
                }

                iSettingsView.setVisibleProgressBar(false);
                iSettingsView.setEnabledSaveBtn(true);
            }
        });
    }

    @Override
    public void updateInfo(final String userName) {

        if (!TextUtils.isEmpty(userName) && mainImageURI != null) {

            iSettingsView.setVisibleProgressBar(true);

            if (isProfileChanged) {

                userId = mAuth.getCurrentUser().getUid();

                StorageReference imagePath = mStorageRef.child("profile_images").child(userId + ".jpg");
                imagePath.putFile(mainImageURI)
                        .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {

                                    storageFirestore(task, userName);

                                } else {
                                    String e = task.getException().getMessage();

                                    iSettingsView.showError("image error: " + e);
                                    iSettingsView.setVisibleProgressBar(false);
                                }
                            }
                        });
            } else

                storageFirestore(null, userName);

        }else {

            iSettingsView.showError("Enter info!");
        }
    }


    private void storageFirestore(Task<UploadTask.TaskSnapshot> task, String userName) {

        Uri download_uri;

        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();
        } else {
            download_uri = mainImageURI;
        }

        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", userName);
        userMap.put("image", download_uri.toString());

        mFirebaseFirestore.collection("Users").document(userId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    iSettingsView.showError("The user settings are updated");
                    iSettingsView.showMainActivity();
                } else {

                    String e = task.getException().getMessage();
                    iSettingsView.showError("firestore error: " + e);
                }

                iSettingsView.setVisibleProgressBar(false);
            }
        });
    }
}
