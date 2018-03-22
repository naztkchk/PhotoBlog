package com.draxvel.simpleblog.data.source.likesData;

import android.app.Activity;

public interface ILikes {

    interface LikeDislikeCallBack {
        void onLike();
        void onDislike();
    }

    interface LikeCountCallBack{
        void OnCount(final int count);
    }

    void showLikeDislike(final Activity activity, final String blogPostId, LikeDislikeCallBack likeDislikeCallBack);
    void getCount(final Activity activity, final String blogPostId, LikeCountCallBack likeCountCallBack);
    void likeDislike(final String blogPostId , LikeDislikeCallBack likeDislikeCallBack);
}
