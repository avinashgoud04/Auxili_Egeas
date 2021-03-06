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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auxili_egeas.MainActivity;
import com.example.auxili_egeas.Model.User;
import com.example.auxili_egeas.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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


public class ProfileFragment extends Fragment {

    CircleImageView image_profile;
    TextView username;
    TextView mobile;
    TextView mail;
    RelativeLayout layout;
    ImageButton editbtn;

    DatabaseReference reference;
    FirebaseUser fuser;

    StorageReference storageReference;

    private static final int IMAGE_REQUEST = 1;
    private Uri imageuri;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-SemiBold.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);
        mobile = view.findViewById(R.id.mobile);
        mail = view.findViewById(R.id.mail);
        layout = view.findViewById(R.id.relativelayout);
        editbtn = view.findViewById(R.id.btn_edit);

        storageReference = FirebaseStorage.getInstance().getReference("profile");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.keepSynced(true);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                mobile.setText(user.getPhone());
                mail.setText(user.getMail());

                if (user.getImageURL().equals("default")) {
                    image_profile.setImageResource(R.drawable.defimg);
                } else {
                    Glide.with(getContext()).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Profile Picture");

                String list[] = new String[]{"Change Photo", "Remove photo"};
                builder.setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == 0) {
                            openImage();
                        } else if (which == 1) {
                            //removing photo
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("imageURL", "default");
                            reference.updateChildren(map);



                        }

                    }
                }).setNegativeButton("Cancel", null);

                AlertDialog alert = builder.create();
                alert.show();

            }
        });


        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfileChange();
            }
        });

        return view;
    }

    private void showProfileChange() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Edit Profile");
        LayoutInflater inflater = LayoutInflater.from(getContext());
        final View edit_layout = inflater.inflate(R.layout.layout_edit, null);
        final EditText profile_username = edit_layout.findViewById(R.id.pname);
        final EditText profile_mobile = edit_layout.findViewById(R.id.pphone);
       // final EditText profile_email = edit_layout.findViewById(R.id.pmail);

        dialog.setView(edit_layout);

        dialog.setPositiveButton("Change Profile", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               if ( TextUtils.isEmpty(profile_mobile.getText().toString()) || TextUtils.isEmpty(profile_username.getText().toString())){
                 Snackbar.make(layout,"All fields are manditory ",Snackbar.LENGTH_SHORT).show();
                 return;
                }
                if (profile_mobile.getText().toString().length()<10){
                    Snackbar.make(layout,"Enter a Valid Mobile No.",Snackbar.LENGTH_SHORT).show();
                    return;
                }

                reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", profile_username.getText().toString());
                map.put("phone", profile_mobile.getText().toString());
               // map.put("mail", profile_email.getText().toString());
                reference.updateChildren(map);

                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts").child(fuser.getUid());
                HashMap<String, Object> m = new HashMap<>();
                m.put("name", profile_username.getText().toString());
                ref.updateChildren(m);


                Snackbar.make(layout,"Profile Updated",Snackbar.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();



    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if (imageuri != null) {
            final StorageReference filereference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageuri));

            uploadTask = filereference.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();

                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                        pd.dismiss();

                    } else {
                        Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();

                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                    ;
                }
            });
        } else {
            Toast.makeText(getContext(), "No image Selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {

                Toast.makeText(getContext(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }
}