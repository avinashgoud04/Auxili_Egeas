package com.example.auxili_egeas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class StartActivity extends AppCompatActivity {

    CircleImageView image_profile;
    TextView username;

    DatabaseReference reference;
    FirebaseUser fuser;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageuri;
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getSupportActionBar().setTitle("Welcome ");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        image_profile=findViewById(R.id.propic);
        username=findViewById(R.id.username);

        storageReference= FirebaseStorage.getInstance().getReference("uploads");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                username.setText(user.getName());

                if(user.getProimage().equals("default")){

                    image_profile.setImageResource(R.drawable.ic_baseline_person_24);
                }
                else
                {
                    Glide.with(StartActivity.this).load(user.getProimage()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Profile Picture");

               String list[]=new String[]{"Change Photo","Remove photo"};
               builder.setItems(list, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       if(which==0)
                       {
                           openImage();
                       }
                       else
                           if(which==1)
                           {
                               //removing photo
                               reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                               HashMap<String, Object> map = new HashMap<>();
                               map.put("proimage", "default");
                               reference.updateChildren(map);

                           }

                   }
               }).setNegativeButton("Cancel",null);

                AlertDialog alert=builder.create();
                alert.show();

            }
        });

    }

    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(StartActivity.this);
        pd.setMessage("Uploading");
        pd.show();

        if(imageuri!=null)
        {
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageuri));

            uploadTask=filereference.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();

                        map.put("proimage", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();

                    }else
                    {
                        Toast.makeText(StartActivity.this,"Failed!",Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StartActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();;
                }
            });
        }
        else
        {
            Toast.makeText(StartActivity.this,"No image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();

            if(uploadTask!=null && uploadTask.isInProgress()){

                Toast.makeText(StartActivity.this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadImage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logout :
                logout();
                return true;
        }
        return false;
    }

    public void logout()
    {
        SpotsDialog loading=new SpotsDialog(StartActivity.this);
        loading.show();
        FirebaseAuth.getInstance().signOut();
        loading.dismiss();
        startActivity(new Intent(StartActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(StartActivity.this);
        builder.setTitle("Exit");
        builder.setMessage("Sure you want to exit ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       StartActivity.super.onBackPressed();
                    }
                }).setNegativeButton("Cancel",null).setCancelable(false);

        AlertDialog alert=builder.create();
        alert.show();
    }
}