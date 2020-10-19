package com.example.auxili_egeas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.ChatActivity;
import com.example.auxili_egeas.Model.Chat;
import com.example.auxili_egeas.Model.User;
import com.example.auxili_egeas.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean inchat;

    String theLastMessage;

    public UserAdapter(Context mContext, List<User> mUsers,boolean inchat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.inchat = inchat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user=mUsers.get(position);
        holder.username.setText(user.getUsername());
        if(user.getImageURL().equals("default"))
        {
            holder.profile_image.setImageResource(R.drawable.defimg);
        }
        else
        {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ChatActivity.class);
                intent.putExtra("name",user.getUsername());
                intent.putExtra("userid",user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView profile_image;

        public ViewHolder(View itemView)
        {
            super(itemView);

            username=itemView.findViewById(R.id.username);
            profile_image=itemView.findViewById(R.id.profile_image);

        }
    }

   /* private void lastMessage(final String userid, final TextView last_msg){

        theLastMessage="default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    Chat chat=snapshot1.getValue(Chat.class);

                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                            chat.getSender().equals(firebaseUser.getUid()) && chat.getReceiver().equals(userid)){

                        theLastMessage=chat.getMessage();

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    */
}
