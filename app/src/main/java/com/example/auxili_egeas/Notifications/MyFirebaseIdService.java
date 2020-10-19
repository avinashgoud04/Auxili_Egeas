package com.example.auxili_egeas.Notifications;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class MyFirebaseIdService extends MyFirebaseMessagingService{

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();

        if(firebaseUser!=null)
        {
            UpdateToken(refreshToken);
        }
    }

    private void UpdateToken(String refreshToken){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Token token=new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(firebaseUser.getUid()).setValue(token);

    }

}
