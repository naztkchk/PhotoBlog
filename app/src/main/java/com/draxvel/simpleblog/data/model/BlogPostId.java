package com.draxvel.simpleblog.data.model;

import com.google.firebase.database.Exclude;

public class BlogPostId {

    @Exclude
    public String BlogPostId;

    public <T extends BlogPostId> T withId(final String id){
        this.BlogPostId = id;
        return (T) this;
    }
}
