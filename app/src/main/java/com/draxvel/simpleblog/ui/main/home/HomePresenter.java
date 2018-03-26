package com.draxvel.simpleblog.ui.main.home;

import android.app.Activity;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.data.source.postsData.IPosts;
import com.draxvel.simpleblog.data.source.postsData.Posts;
import com.draxvel.simpleblog.ui.main.adapter.BlogRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter {

    private IHomeView iHomeView;
    private Activity mActivity;

    private Posts posts;

    private List<BlogPost> blogPostList;

    private BlogRecyclerAdapter blogRecyclerAdapter;

    HomePresenter(IHomeView iHomeView, Activity activity){

        this.iHomeView = iHomeView;
        this.mActivity = activity;

        posts = new Posts();

        blogPostList = new ArrayList<>();


        blogRecyclerAdapter = new BlogRecyclerAdapter(blogPostList, activity);

        iHomeView.setAdapter(blogRecyclerAdapter);
    }

     public void setData(){

        posts.updateFeed(mActivity, new IPosts.UpdateFeedCallBack() {
            @Override
            public void OnUpdate(BlogPost blogPost, boolean isFirst) {
                if(isFirst)
                    blogPostList.add(0, blogPost);
                else blogPostList.add(blogPost);

                blogRecyclerAdapter.notifyDataSetChanged();
            }
        });

     }

     public void loadMorePost() {

         posts.loadMorePost(mActivity, new IPosts.LoadMorePostsCallBack() {
             @Override
             public void OnLoad(final BlogPost blogPost) {
                 blogPostList.add(blogPost);
                 blogRecyclerAdapter.notifyDataSetChanged();
             }
         });
     }
}
