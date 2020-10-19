package com.example.auxili_egeas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.ChatActivity;
import com.example.auxili_egeas.Model.Chat;
import com.example.auxili_egeas.Model.User;
import com.example.auxili_egeas.Notifications.APIService;
import com.example.auxili_egeas.Notifications.Client;
import com.example.auxili_egeas.Notifications.Data;
import com.example.auxili_egeas.Notifications.MyResponse;
import com.example.auxili_egeas.Notifications.NotificationSender;
import com.example.auxili_egeas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_Right=1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;


    FirebaseUser fuser;

    public  ChatAdapter(Context mContext, List<Chat> mChat,String imageurl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageurl=imageurl;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        if(viewType==MSG_TYPE_Right) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

            return new ChatAdapter.ViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

            return new ChatAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {

        Chat chat=mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.drawable.defimg);
        }
        else
        {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }



    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public ImageView profile_image;

        public TextView txt_seen;

        public ViewHolder(View itemView)
        {
            super(itemView);

            show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_Right;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }


}
