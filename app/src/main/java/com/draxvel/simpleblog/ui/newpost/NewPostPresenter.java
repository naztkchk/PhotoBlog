package com.draxvel.simpleblog.ui.newpost;

import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.draxvel.simpleblog.data.source.postsData.IPosts;
import com.draxvel.simpleblog.data.source.postsData.Posts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class NewPostPresenter implements INewPostPresenter{

    private INewPostView iNewPostView;

    private Uri mainImageURI = null;
    private Bitmap compressedImageFile;


    public NewPostPresenter(INewPostView iNewPostView) {
        this.iNewPostView = iNewPostView;
    }

    public void setMainImageURI(Uri mainImageURI) {
        this.mainImageURI = mainImageURI;
    }

    @Override
    public void publishPost(final String desc) {
        if (!TextUtils.isEmpty(desc) && mainImageURI != null) {

            iNewPostView.setVisibleProgressBar(true);

            final Posts posts = new Posts();

            posts.saveImages(mainImageURI, imageToByteArray(mainImageURI), new IPosts.SaveImageCallBack() {

                @Override
                public void OnSave(String image_url, String thumb_url) {
                    posts.publishPost(image_url, thumb_url, desc, new IPosts.PublishPostCallBack() {
                        @Override
                        public void onPublish() {
                            iNewPostView.showMainActivity();
                            iNewPostView.setVisibleProgressBar(false);
                        }

                        @Override
                        public void onFailure(String msg) {
                            iNewPostView.showError(msg);
                            iNewPostView.setVisibleProgressBar(false);
                        }
                    });
                }

                @Override
                public void OnFailure(final String msg) {
                    iNewPostView.showError(msg);
                    iNewPostView.setVisibleProgressBar(false);
                }
            });
        }else {
            iNewPostView.showError("Enter info!");
        }
    }

    private byte[] imageToByteArray(Uri imageURI){
        File imageFile = new File(imageURI.getPath());
        try {
            compressedImageFile = new Compressor(((NewPostActivity)iNewPostView)
                    .getApplicationContext())
                    .setMaxHeight(200)
                    .setMaxWidth(200)
                    .setQuality(10)
                    .compressToBitmap(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return  baos.toByteArray();
    }
}
