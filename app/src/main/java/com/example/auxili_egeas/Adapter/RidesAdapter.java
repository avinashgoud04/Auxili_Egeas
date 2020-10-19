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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.ChatActivity;
import com.example.auxili_egeas.Model.Post;
import com.example.auxili_egeas.Notifications.APIService;
import com.example.auxili_egeas.Notifications.Client;
import com.example.auxili_egeas.Notifications.Data;
import com.example.auxili_egeas.Notifications.MyResponse;
import com.example.auxili_egeas.Notifications.NotificationSender;
import com.example.auxili_egeas.Notifications.Token;
import com.example.auxili_egeas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidesAdapter  extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mPosts;
    Button chatbtn;
    Button bookbtn;
    Post post1;

    private APIService apiService;

    public RidesAdapter(Context mContext, List<Post> mPosts) {
        this.mContext = mContext;
        this.mPosts = mPosts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.posts_item,parent,false);
        chatbtn=view.findViewById(R.id.chatbtn);
        bookbtn=view.findViewById(R.id.bookbtn);
        apiService= Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        return new RidesAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final Post post=mPosts.get(position);
        holder.bikemodel.setText(post.getBmodel());
        holder.bikefare.setText(post.getBfair()+" Rs.");
        holder.biketime.setText(post.getBtime()+" hrs.");
        holder.owner.setText(post.getName());
        if(post.getImageURL().equals("default"))
        {
            holder.bikepic.setImageResource(R.drawable.defimg);
        }
        else
        {
            Glide.with(mContext).load(post.getImageURL()).into(holder.bikepic);
        }

        bookbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,"Clicked!",Toast.LENGTH_SHORT).show();

                post1=mPosts.get(position);
                FirebaseDatabase.getInstance().getReference().child("Tokens").child(post1.getId().trim()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String usertoken=snapshot.getValue(String.class);
                        sendNotifications(usertoken,"New Order","You have a new order !");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


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
        public TextView owner;
        public ImageView bikepic;

        public ViewHolder(View itemView)
        {

            super(itemView);
            bikefare=itemView.findViewById(R.id.bike_fair);
            bikemodel=itemView.findViewById(R.id.bike_model);
            biketime=itemView.findViewById(R.id.bike_time);
            bikepic=itemView.findViewById(R.id.bike_image);
            owner=itemView.findViewById(R.id.bike_user);

        }
    }




    public void sendNotifications(String usertoken,String title,String message)
    {
        Data data=new Data(title,message);
        NotificationSender sender=new NotificationSender(data,usertoken);

        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200)
                {
                    if(response.body().success!=1)
                    {
                        Toast.makeText(mContext,"Failed !",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

}
