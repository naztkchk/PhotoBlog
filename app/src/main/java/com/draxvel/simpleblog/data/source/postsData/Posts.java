package com.draxvel.simpleblog.data.source.postsData;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Posts implements IPosts{

    private DocumentSnapshot lastVisible;
    private boolean isFirstPageFirstLoad = true;

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

    @Override
    public void updateFeed(Activity activity, final UpdateFeedCallBack updateFeedCallBack) {
        Query firstQuery =
                FirebaseFirestore.getInstance()
                        .collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);

        firstQuery.addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(isFirstPageFirstLoad){
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);
                    Log.i("sss", lastVisible.getString("desc"));
                }

                if(!documentSnapshots.isEmpty()){

                    for(DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                        if(doc.getType() == DocumentChange.Type.ADDED){

                            String blogPostId = doc.getDocument().getId();

                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);
                            if(isFirstPageFirstLoad){
                                updateFeedCallBack.OnUpdate(blogPost, false);
                            }else
                            {
                                updateFeedCallBack.OnUpdate(blogPost, true);
                            }
                        }
                        isFirstPageFirstLoad = false;
                    }
                }else Log.i("sss", "doc = nullupdateFeed");
            }
        });
    }

    @Override
    public void updateFeedForCurrentUser(Activity activity, final UpdateFeedCallBack updateFeedCallBack) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query firstQuery =
                FirebaseFirestore.getInstance()
                        .collection("Posts").whereEqualTo("user_id", userId)
                        .orderBy("timestamp", Query.Direction.DESCENDING).limit(3);

        firstQuery.addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(documentSnapshots != null){
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();

                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                            updateFeedCallBack.OnUpdate(blogPost, false);
                        }
                }
            }
        }});

    }

    @Override
    public void loadMorePost(Activity activity, final LoadMorePostsCallBack loadMorePostsCallBack) {

        Log.i("sss1", lastVisible.getString("desc"));

        Query nextQuery = FirebaseFirestore.getInstance().collection("Posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        nextQuery.addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()) {

                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);

                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            String blogPostId = doc.getDocument().getId();

                            BlogPost blogPost = doc.getDocument().toObject(BlogPost.class).withId(blogPostId);

                            loadMorePostsCallBack.OnLoad(blogPost);
                        }
                    }
                }else Log.i("sss", "doc = null");
            }
        });
    }
}
