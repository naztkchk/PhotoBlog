package com.draxvel.simpleblog.ui.main.account;

import android.app.Activity;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.data.source.postsData.IPosts;
import com.draxvel.simpleblog.data.source.postsData.Posts;
import com.draxvel.simpleblog.ui.main.adapter.BlogRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountPresenter implements IAccountPresenter {

    private Activity activity;
    private IAccountView iAccountView;

    private Posts posts;
    private List<BlogPost> blogPostList;
    private BlogRecyclerAdapter blogRecyclerAdapter;

    public AccountPresenter(IAccountView iAccountView, Activity activity) {
        this.iAccountView = iAccountView;
        this.activity = activity;

        posts = new Posts();

        blogPostList = new ArrayList<>();

        blogRecyclerAdapter = new BlogRecyclerAdapter(blogPostList, activity);

        iAccountView.setAdapter(blogRecyclerAdapter);
    }

    public void setData() {
        posts.updateFeedForCurrentUser(activity, new IPosts.UpdateFeedCallBack() {
            @Override
            public void OnUpdate(BlogPost blogPost, boolean isFirst) {
                 blogPostList.add(blogPost);
                blogRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }
}
