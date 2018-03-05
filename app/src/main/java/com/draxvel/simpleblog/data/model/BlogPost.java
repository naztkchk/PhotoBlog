package com.draxvel.simpleblog.data.model;

import java.sql.Timestamp;

public class BlogPost {

    public String desc, image_url, thumb_url, user_id;

    BlogPost(){}

    public BlogPost(String user_id, String image_url, String thumb_url, String desc, Timestamp timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.thumb_url = thumb_url;
        this.desc = desc;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
