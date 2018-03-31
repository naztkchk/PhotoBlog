package com.draxvel.simpleblog.data.source.likesData;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Likes implements ILikes{


    @Override
    public void showLikeDislike(final Activity activity, final String blogPostId, final LikeDislikeCallBack likeDislikeCallBack) {

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance().collection("Posts/"+blogPostId+"/likes")
                .document(currentUserId).addSnapshotListener(activity, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){
                    likeDislikeCallBack.onLike();
                }
                else{
                    likeDislikeCallBack.onDislike();
                }
            }
        });
    }

    @Override
    public void getCount(Activity activity, String blogPostId, final LikeCountCallBack likeCountCallBack) {
        FirebaseFirestore.getInstance()
                .collection("Posts/"+blogPostId+"/likes").addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(!documentSnapshots.isEmpty()){

                    int count  = documentSnapshots.size();
                    likeCountCallBack.OnCount(count);
                }else{
                    likeCountCallBack.OnCount(0);
                }
            }
        });
    }

    @Override
    public void likeDislike(final String blogPostId, LikeDislikeCallBack likeDislikeCallBack) {

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("Posts/"+blogPostId+"/likes")
                .document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(!task.getResult().exists()){

                    Map<String, Object> likeMap = new HashMap<>();
                    likeMap.put("timestamp", FieldValue.serverTimestamp());

                    FirebaseFirestore.getInstance().collection("Posts/"+blogPostId+"/likes")
                            .document(currentUserId).set(likeMap);
                }else{

                    FirebaseFirestore.getInstance().collection("Posts/"+blogPostId+"/likes")
                            .document(currentUserId).delete();
                }
            }
        });
    }
}
