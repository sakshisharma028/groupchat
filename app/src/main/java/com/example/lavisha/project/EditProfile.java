package com.example.lavisha.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class EditProfile extends AppCompatActivity {

    ImageView editdp;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    EditText editUsername;
    Integer CHOOSE_IMAGE=123;
    DatabaseReference databaseReference;
    Button btnSaveChanges;
    ImageButton btnOpenGallery;
    Uri profileimguri;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_profile);
        editdp=findViewById(R.id.imgEditProfilePic);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        editUsername=findViewById(R.id.etEditUsername);
        firebaseStorage=FirebaseStorage.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        storageReference=firebaseStorage.getReference();
        btnSaveChanges=findViewById(R.id.btnSaveChanges);
        btnOpenGallery=findViewById(R.id.btnOpenGallery);
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Photo"),CHOOSE_IMAGE);
            }
        });
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              uploadImageToFirebase();
            }
        });

    }

    private void uploadImageToFirebase() {

        if(profileimguri!=null) {

            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference ref=storageReference.child("images/"+UUID.randomUUID().toString());
            ref.putFile(profileimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(EditProfile.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                       /*FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                        final String downloadUri=task.getResult().toString();
                        databaseReference=FirebaseDatabase.getInstance().getReference("Users");
                        databaseReference.child(firebaseUser.getUid()).child("imgurl").setValue(downloadUri);*/
                        /*databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User user=dataSnapshot.getValue(User.class);

                                    HashMap<String,Object>hashMap=new HashMap<>();
                                    hashMap.put("imgurl",downloadUri);
                                    databaseReference.getRef().updateChildren(hashMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/

                    }
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.e("TAG","Image Upload:"+ e.getMessage());
                    Toast.makeText(EditProfile.this, "Failed to upload the image", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&data.getData()!=null){
            profileimguri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),profileimguri);
                editdp.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
