package com.draxvel.simpleblog.ui.main.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draxvel.simpleblog.R;
import com.draxvel.simpleblog.data.model.BlogPost;
import com.draxvel.simpleblog.data.source.likesData.ILikes;
import com.draxvel.simpleblog.data.source.likesData.Likes;
import com.draxvel.simpleblog.data.source.usersData.IUsers;
import com.draxvel.simpleblog.data.source.usersData.Users;
import com.draxvel.simpleblog.util.DateTimeConverter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blogPostList;
    public Context context;
    public Activity activity;
    private Users users;
    private Likes likes;

    public BlogRecyclerAdapter(List<BlogPost> blogPostList, Activity activity){
        this.blogPostList = blogPostList;
        this.activity = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blog_list_item, parent, false);

        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String blogPostId = blogPostList.get(position).BlogPostId;
        String desc_data = blogPostList.get(position).getDesc();
        holder.setDescText(desc_data);

        final String image_url = blogPostList.get(position).getImage_url();
        final String thumb_url = blogPostList.get(position).getThumb_url();
        holder.setBlogImage(image_url, thumb_url);

        String user_id = blogPostList.get(position).getUser_id();

        users = new Users();
        users.getUserById(user_id, new IUsers.GetUserByIdCallBack() {
            @Override
            public void onGet(String name, String image) {
                holder.setUserName(name);
                holder.setUserImage(image);
            }
        });

        holder.setTime(DateTimeConverter.DateToString(blogPostList.get(position).getTimestamp()));


        //real time check like exist
        likes.showLikeDislike(activity, blogPostId, new ILikes.LikeDislikeCallBack() {
            @Override
            public void onLike() {
                holder.like_iv.setImageResource(R.mipmap.fav_accent_ico);
            }

            @Override
            public void onDislike() {
                holder.like_iv.setImageResource(R.mipmap.fav_ico);
            }
        });


        //real time likes count
        likes.getCount(activity, blogPostId, new ILikes.LikeCountCallBack() {
            @Override
            public void OnCount(int count) {
                holder.updateLikeCount(count);
            }
        });


        holder.like_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes.likeDislike(blogPostId, new ILikes.LikeDislikeCallBack() {
                    @Override
                    public void onLike() {
                        //hit like
                    }

                    @Override
                    public void onDislike() {
                        //delete like
                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return blogPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView userNameView;
        private CircleImageView userImageView;

        private TextView dateView;

        private ImageView blogImageView;
        private TextView descView;

        private ImageView like_iv;
        private TextView like_count_iv;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            like_iv = mView.findViewById(R.id.like_iv);
            like_count_iv = mView.findViewById(R.id.like_count_tv);
        }

        public void setDescText(String text){
            descView = mView.findViewById(R.id.desc_tv);
            descView.setText(text);
        }

        public void setBlogImage(String download_uri, String thumb_url){
            blogImageView = mView.findViewById(R.id.imageView);
            Glide.with(context)
                    .load(download_uri)
                    .thumbnail(Glide.with(context).load(thumb_url))
                            .into(blogImageView);
        }

        public void setUserName(String userName){
            userNameView = mView.findViewById(R.id.username_tv);
            userNameView.setText(userName);
        }

        public void setUserImage(String download_uri){
            userImageView = mView.findViewById(R.id.user_iv);

            RequestOptions placaholderOptions  = new RequestOptions();
            placaholderOptions.placeholder(R.mipmap.account_ic);

            Glide.with(context)
                    .applyDefaultRequestOptions(placaholderOptions)
                    .load(download_uri)
                    .into(userImageView);
        }

        public void setTime(String time){
            dateView = mView.findViewById(R.id.time_tv);
            dateView.setText(time);
        }

        public void updateLikeCount(int count){
            like_count_iv = mView.findViewById(R.id.like_count_tv);
            like_count_iv.setText(count+ "likes");
        }
    }
}
