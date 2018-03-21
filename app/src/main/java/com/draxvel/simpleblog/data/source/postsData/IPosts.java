package com.draxvel.simpleblog.data.source.postsData;

import android.app.Activity;
import android.net.Uri;

import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.data.source.usersData.IUsers;

import java.util.List;

public interface IPosts {

    interface SaveImageCallBack {
        void OnSave(final String image_url, final String thumb_url);
        void OnFailure(final String msg);
    }

    interface PublishPostCallBack{
        void onPublish();
        void onFailure(final String msg);
    }

    interface UpdateFeedCallBack{
        void OnUpdate(final BlogPost blogPost, final boolean isFirst);
    }

    interface LoadMorePostsCallBack{
        void OnLoad(final BlogPost blogPost);
    }

    void saveImages(final Uri imageUrl, final byte[] image, SaveImageCallBack saveImagesCallBack);
    void publishPost(final String image_url, final String thumb_url, String desc, PublishPostCallBack publishPostCallBack);
    void updateFeed(Activity activity, UpdateFeedCallBack updateFeedCallBack);
    void loadMorePost(Activity activity, LoadMorePostsCallBack loadMorePostsCallBack);
}
