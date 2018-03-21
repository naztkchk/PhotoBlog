package com.draxvel.simpleblog.data.source.postsData;

import android.net.Uri;

public interface IPosts {

    interface SaveImageCallBack {
        void OnSave(final String image_url, final String thumb_url);
        void OnFailure(final String msg);
    }

    interface PublishPostCallBack{
        void onPublish();
        void onFailure(final String msg);
    }
    void saveImages(final Uri imageUrl, final byte[] image, SaveImageCallBack saveImagesCallBack);
    void publishPost(final String image_url, final String thumb_url, String desc, PublishPostCallBack publishPostCallBack);
}
