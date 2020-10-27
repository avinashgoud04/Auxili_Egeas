package com.example.auxili_egeas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
     Button forgotpassbtn;
     EditText send_email;

     FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send_email=findViewById(R.id.send_email);
        forgotpassbtn=findViewById(R.id.forgotpasswordloginbtn);

        firebaseAuth=FirebaseAuth.getInstance();

        forgotpassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=send_email.getText().toString();
                if (email.equals("")){
                    Toast.makeText(ForgotPassword.this, "Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ForgotPassword.this, "Please check your Email, reset password link has been sent.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this,LoginActivity.class));
                            }else {
                                String error=task.getException().getMessage();
                                Toast.makeText(ForgotPassword.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}