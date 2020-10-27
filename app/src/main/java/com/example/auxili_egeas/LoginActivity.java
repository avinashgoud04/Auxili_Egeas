package com.example.auxili_egeas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {


    Button login;
    Toolbar toolbar;
    ConstraintLayout login_layout;
    TextView signup1,signup2,forgotpassword;
    EditText email,logpassword;
    FirebaseAuth fauth;
    FirebaseUser fuser;

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-SemiBold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_login);

        login=findViewById(R.id.loginbutton);

        signup2=findViewById(R.id.tvsignuphint);
        signup1=findViewById(R.id.tvsignup);
        email=findViewById(R.id.email);
        logpassword=findViewById(R.id.passwordLogin);

        login_layout=findViewById(R.id.loginlayout);

        forgotpassword=findViewById(R.id.forgotpasswordlogin);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Auxili Egeas");



         fauth=FirebaseAuth.getInstance();

        fuser=FirebaseAuth.getInstance().getCurrentUser();


       if(fuser!=null)
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }




        signup2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
              finish();
          }
      });
        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
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
              final SpotsDialog loadingdialog= new SpotsDialog(LoginActivity.this);
              loadingdialog.show();
              fauth.signInWithEmailAndPassword(email.getText().toString(),logpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                  @Override
                  public void onSuccess(AuthResult authResult) {
                      loadingdialog.dismiss();
                      Toast.makeText(getApplicationContext(),"Login Successfull !",Toast.LENGTH_SHORT).show();
                     startActivity(new Intent(LoginActivity.this,MainActivity.class));
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

      forgotpassword.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
          Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
          startActivity(intent);
          }
      });


    }


    }
