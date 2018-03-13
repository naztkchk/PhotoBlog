package com.draxvel.simpleblog.ui.main.home;

import android.util.Log;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.ui.main.home.adapter.BlogRecyclerAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter {

    private IHomeView iHomeView;

    private FirebaseFirestore firebaseFirestore;

    private DocumentSnapshot lastVisible;
    private List<BlogPost> blogPostList;

    private BlogRecyclerAdapter blogRecyclerAdapter;


    HomePresenter(IHomeView iHomeView){

        this.iHomeView = iHomeView;

        firebaseFirestore = FirebaseFirestore.getInstance();

        blogPostList = new ArrayList<>();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blogPostList);

        iHomeView.setAdapter(blogRecyclerAdapter);
    }

     public void setData(){

         Query firstQuery = firebaseFirestore.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING).limit(3);

         firstQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                 lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);

                 if(!documentSnapshots.isEmpty()){

                     for(DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                         if(doc.getType() == DocumentChange.Type.ADDED){

                             BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                             blogPostList.add(blogPost);

                             blogRecyclerAdapter.notifyDataSetChanged();

                         }
                     }
                 }else {
                     Log.i("Home", "doc = null");
                 }

             }
         });
     }

     public void loadMorePost(){

         Query nextQuery = firebaseFirestore.collection("Posts")
                 .orderBy("timestamp", Query.Direction.DESCENDING)
                 .startAfter(lastVisible)
                 .limit(3);

         nextQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                 if(!documentSnapshots.isEmpty()){

                     lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size()-1);

                     for(DocumentChange doc: documentSnapshots.getDocumentChanges()) {
                         if(doc.getType() == DocumentChange.Type.ADDED){

                             BlogPost blogPost = doc.getDocument().toObject(BlogPost.class);
                             blogPostList.add(blogPost);

                             blogRecyclerAdapter.notifyDataSetChanged();

                         }
                     }
                 }else {
                     Log.i("Home", "doc = null");
                 }

             }
         });
     }
}
