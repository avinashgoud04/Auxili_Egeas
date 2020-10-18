package com.example.auxili_egeas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.auxili_egeas.Model.Post;
import com.example.auxili_egeas.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import dmax.dialog.SpotsDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {
    Button signUpbtn;
    Toolbar toolbar;
    EditText usernameReg,phoneReg,passwordReg,emailreg;
    TextView login;
    FirebaseAuth fauth;
    FirebaseDatabase db;
    DatabaseReference users,posts;

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

        setContentView(R.layout.activity_sign_up);

        signUpbtn=findViewById(R.id.registerbutton);
        usernameReg=findViewById(R.id.Usernameregister);
        passwordReg=findViewById(R.id.passwordRegister);
        phoneReg=findViewById(R.id.phoneregister);
        login=findViewById(R.id.tvlogin1);
        emailreg=findViewById(R.id.emailid);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Auxili Egeas");

        fauth=FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        users=db.getReference("Users");
        posts=FirebaseDatabase.getInstance().getReference("Posts");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();
            }
        });

        signUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(emailreg.getText().toString()))
                {

                    // Snackbar.make(root,"Please enter an email address",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please enter an email address",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(passwordReg.getText().toString()))
                {

                    // Snackbar.make(root,"Please enter a password",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please enter a password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(usernameReg.getText().toString()))
                {

                    // Snackbar.make(root,"Please enter a name",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please enter a name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneReg.getText().toString()))
                {

                    // Snackbar.make(root,"Please enter a phone number",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please enter a phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwordReg.getText().toString().length()<6)
                {

                    // Snackbar.make(root,"Password is too short!",Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Password is too short!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (phoneReg.getText().toString().length()<10){
                    Toast.makeText(getApplicationContext(),"Enter a Valid Mobile No.",Toast.LENGTH_SHORT).show();
                    return;
                }


                //Register now
                final SpotsDialog loading=new SpotsDialog(SignUpActivity.this);
                loading.show();

                fauth.createUserWithEmailAndPassword(emailreg.getText().toString(),passwordReg.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                //making 1st letter capital
                                String tmpusername=usernameReg.getText().toString();
                                String username=tmpusername.substring(0,1).toUpperCase()+tmpusername.substring(1);

                                FirebaseUser fuser=fauth.getCurrentUser();

                                //saving user data
                                User user=new User();
                                Post post=new Post();
                                post.setImageURL("default");
                                post.setId(fuser.getUid());
                                post.setBfair("default");
                                post.setBmodel("default");
                                post.setBtime("default");
                                post.setName(username);

                                user.setMail(emailreg.getText().toString());
                                user.setPassword(passwordReg.getText().toString());
                                user.setUsername(username);
                                user.setPhone(phoneReg.getText().toString());
                                user.setImageURL("default");
                                user.setId(fuser.getUid());

                                posts.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(post)
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        });

                                users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                //Snackbar.make(root,"Registered Successfully!!",Snackbar.LENGTH_SHORT).show();

                                                Toast.makeText(getApplicationContext(),"Registered Successfully!!",Toast.LENGTH_SHORT).show();
                                                loading.dismiss();
                                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                                finish();


                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Snackbar.make(root,"Failed "+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                                loading.dismiss();
                                                Toast.makeText(getApplicationContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //Snackbar.make(root,"Failed "+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                loading.dismiss();
                                Toast.makeText(getApplicationContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}