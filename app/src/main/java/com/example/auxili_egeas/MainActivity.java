package com.example.auxili_egeas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


   Button login;
    TextView signup2;EditText email,logpassword;
    FirebaseAuth fauth;
    FirebaseUser fuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login=findViewById(R.id.loginbutton);

        signup2=findViewById(R.id.tvsignuphint);
        email=findViewById(R.id.email);
        logpassword=findViewById(R.id.passwordLogin);

      fauth=FirebaseAuth.getInstance();

      fuser=FirebaseAuth.getInstance().getCurrentUser();

      //Auto login
      if(fuser!=null)
      {
          startActivity(new Intent(MainActivity.this,StartActivity.class));
          finish();
      }



      signup2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(MainActivity.this,SignUpActivity.class));
              finish();
          }
      });

      login.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              signup2.setEnabled(false);


              if(TextUtils.isEmpty(email.getText().toString()))
              {

                 // Snackbar.make(root,"Please enter an email address",Snackbar.LENGTH_SHORT).show();
                  Toast.makeText(getApplicationContext(),"Please enter an email address",Toast.LENGTH_SHORT).show();
                  return;
              }
              if(TextUtils.isEmpty(logpassword.getText().toString()))
              {

                 // Snackbar.make(root,"Please enter a password",Snackbar.LENGTH_SHORT).show();
                  Toast.makeText(getApplicationContext(),"Please enter a password",Toast.LENGTH_SHORT).show();
                  return;
              }
              //waiting dialog
              final SpotsDialog loadingdialog= new SpotsDialog(MainActivity.this);
              loadingdialog.show();
              fauth.signInWithEmailAndPassword(email.getText().toString(),logpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {
                      loadingdialog.dismiss();
                      Toast.makeText(getApplicationContext(),"Login Successfull !",Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(MainActivity.this,StartActivity.class));
                     finish();
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      loadingdialog.dismiss();
                      Toast.makeText(getApplicationContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();

                      signup2.setEnabled(true);

                  }
              });

          }
      });

    }


    }
