package com.example.auxili_egeas.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.Model.Post;
import com.example.auxili_egeas.Model.User;
import com.example.auxili_egeas.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.app.Activity.RESULT_OK;

public class RentFragment extends Fragment {

    CircleImageView bikepic;
    EditText bikemodel,biketime,bikefair;
    Button postbike,editbtn,cancelbtn;

    DatabaseReference reference;
    DatabaseReference posts;
    FirebaseUser fuser;

    StorageReference storageReference;

    private static final int IMAGE_REQUEST=1;
    private Uri imageuri;
    private StorageTask uploadTask;
    String bikepicture="default";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-SemiBold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        View view=inflater.inflate(R.layout.fragment_rent, container, false);

        bikemodel=view.findViewById(R.id.bike_model);
        biketime=view.findViewById(R.id.ride_time);
        bikefair=view.findViewById(R.id.bike_fair);
        editbtn=view.findViewById(R.id.edit);
        cancelbtn=view.findViewById(R.id.cancel);

        postbike=view.findViewById(R.id.post);
        bikepic=view.findViewById(R.id.bike_image);
        storageReference= FirebaseStorage.getInstance().getReference("bikes");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Posts").child(fuser.getUid());
        posts=FirebaseDatabase.getInstance().getReference("Posts");


            reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post=snapshot.getValue(Post.class);

                if(post.getImageURL().equals("default")){
                    bikepic.setImageResource(R.drawable.ic_baseline_motorcycle_24);
                }
                else
                {
                    Glide.with(getContext()).load(post.getImageURL()).into(bikepic);
                    bikepicture=post.getImageURL();
                    if(post.getBmodel()!=null && !post.getBmodel().equals("default"))
                    {
                        bikepicture=post.getImageURL();
                        bikefair.setText(post.getBfair());
                        bikemodel.setText(post.getBmodel());
                        biketime.setText(post.getBtime());
                        enableedit();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        bikepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Bike Picture");

                String list[] = new String[]{"Choose Photo"};
                builder.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            openImage();
                        }
                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });



        postbike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(bikemodel.getText().toString()) || TextUtils.isEmpty(bikefair.getText().toString()) || TextUtils.isEmpty(biketime.getText().toString()))
                {

                    Toast.makeText(getContext(),"All fields are manditory !",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(bikepicture.equals("default"))
                {
                    Toast.makeText(getContext(),"Please input your bike picture",Toast.LENGTH_SHORT).show();
                    return;
                }


                Post post=new Post();
                post.setBmodel(bikemodel.getText().toString());
                post.setBfair(bikefair.getText().toString());
                post.setBtime(biketime.getText().toString());
                post.setImageURL(bikepicture);
                post.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());



                posts.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getContext(),"Posted Successfully!!",Toast.LENGTH_SHORT).show();

                                enableedit();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });



            }
        });

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enablepost();

            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Post post=new Post();
                post.setBmodel("default");
                post.setBfair("default");
                post.setBtime("default");
                post.setImageURL(bikepicture);
                post.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                posts.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(post)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Failed "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                enablepost();

            }
        });

        return view;


    }

    private void enablepost()
    {
        editbtn.setVisibility(View.GONE);
        cancelbtn.setVisibility(View.GONE);
        bikefair.setEnabled(true);
        bikemodel.setEnabled(true);
        biketime.setEnabled(true);
        bikepic.setEnabled(true);
        postbike.setVisibility(View.VISIBLE);
    }

    private void enableedit()
    {
        bikefair.setEnabled(false);
        bikemodel.setEnabled(false);
        biketime.setEnabled(false);
        bikepic.setEnabled(false);
        postbike.setVisibility(View.GONE);
        editbtn.setVisibility(View.VISIBLE);
        cancelbtn.setVisibility(View.VISIBLE);
    }

    private void openImage(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd=new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageuri!=null)
        {
            final StorageReference filereference=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageuri));

            uploadTask=filereference.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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

                        reference = FirebaseDatabase.getInstance().getReference("Posts").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();

                        map.put("imageURL", mUri);
                        reference.updateChildren(map);
                        bikepicture=mUri;
                        pd.dismiss();

                    }else
                    {
                        Toast.makeText(getContext(),"Failed!",Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();;
                }
            });
        }
        else
        {
            Toast.makeText(getContext(),"No image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();

            if(uploadTask!=null && uploadTask.isInProgress()){

                Toast.makeText(getContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else
            {
                uploadImage();
            }
        }
    }



}