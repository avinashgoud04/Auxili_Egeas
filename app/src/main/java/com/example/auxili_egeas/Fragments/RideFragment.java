package com.example.auxili_egeas.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.auxili_egeas.Adapter.RidesAdapter;
import com.example.auxili_egeas.Model.Post;
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

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class RideFragment extends Fragment {

    private RecyclerView recyclerView;
    private RidesAdapter ridesAdapter;
    private LinearLayoutManager linearLayoutManager;
    List<Post> mPosts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-SemiBold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        View view=inflater.inflate(R.layout.fragment_ride, container, false);

        linearLayoutManager=new LinearLayoutManager(getActivity());

        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mPosts=new ArrayList<>();

        readPosts();


        UpdateToken(FirebaseInstanceId.getInstance().getToken());
        return view;
    }

    private void UpdateToken(String refreshToken){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Token token=new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);

    }

    private void readPosts(){


        final FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mPosts.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){

                    Post post=snapshot1.getValue(Post.class);

                    assert post!=null;

                    assert firebaseUser!=null;

                    if(!post.getId().equals(firebaseUser.getUid()) && !post.getBfair().equals("default")){
                        mPosts.add(post);
                    }

                }

                ridesAdapter=new RidesAdapter(getContext(),mPosts);
                recyclerView.setAdapter(ridesAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}