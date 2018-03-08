package com.draxvel.simpleblog.ui.main.home;

import android.util.Log;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.ui.main.home.adapter.BlogRecyclerAdapter;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter {

    private IHomeView iHomeView;

    private FirebaseFirestore firebaseFirestore;
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

         firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
             @Override
             public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                 if(documentSnapshots!=null){

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
