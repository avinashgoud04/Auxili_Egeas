package com.example.auxili_egeas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.ChatActivity;
import com.example.auxili_egeas.Model.Post;
import com.example.auxili_egeas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RidesAdapter  extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    Button chatbtn;

    public RidesAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.posts_item,parent,false);
        chatbtn=view.findViewById(R.id.chatbtn);

        return new RidesAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Post post=mPosts.get(position);
        holder.bikemodel.setText(post.getBmodel());
        holder.bikefare.setText(post.getBfair()+" Rs.");
        holder.biketime.setText(post.getBtime()+" hrs.");
        if(post.getImageURL().equals("default"))
        {
            holder.bikepic.setImageResource(R.drawable.defimg);
        }
        else
        {
            Glide.with(mContext).load(post.getImageURL()).into(holder.bikepic);
        }

       chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra("userid",post.getId());
                mContext.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView bikefare;
        public TextView biketime;
        public TextView bikemodel;
        public ImageView bikepic;

        public ViewHolder(View itemView)
        {
            super(itemView);

            bikefare=itemView.findViewById(R.id.bike_fair);
            bikemodel=itemView.findViewById(R.id.bike_model);
            biketime=itemView.findViewById(R.id.bike_time);
            bikepic=itemView.findViewById(R.id.bike_image);


        }
    }


}
