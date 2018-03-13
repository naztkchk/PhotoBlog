package com.draxvel.simpleblog.ui.main.home.adapter;

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
import com.draxvel.simpleblog.util.DateTimeConverter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blogPostList;
    public Context context;

    public BlogRecyclerAdapter(List<BlogPost> blogPostList){
        this.blogPostList = blogPostList;
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
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String desc_data = blogPostList.get(position).getDesc();
        holder.setDescText(desc_data);

        String image_url = blogPostList.get(position).getImage_url();
        String thumb_url = blogPostList.get(position).getThumb_url();
        holder.setBlogImage(image_url, thumb_url);

        String user_id = blogPostList.get(position).getUser_id();

        FirebaseFirestore.getInstance().collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    holder.setUserName(task.getResult().getString("name"));
                    holder.setUserImage(task.getResult().getString("image"));
                }
            }
        });

        holder.setTime(DateTimeConverter.DateToString(blogPostList.get(position).getTimestamp()));
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


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
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
    }
}
