package com.example.lavisha.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    String email,pass;
    private FirebaseAuth firebaseAuth;
    private int CHOOSE_IMAGE=123;
    HashMap<String,String>hashMap = new HashMap<>();;
    private StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    CircleImageView profilepic;
    String password1;
    String confpassword;
     Uri profileimguri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_new_account);
        final EditText username=findViewById(R.id.etUsername);
        final EditText emailid=findViewById(R.id.etEmailSignUp);
        storageReference=FirebaseStorage.getInstance().getReference();
        profilepic = findViewById(R.id.imgViewSignUp);
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile Photo"),CHOOSE_IMAGE);
            }
        });
        @SuppressLint("WrongViewCast")  RadioButton radioButton=findViewById(R.id.radioBtnGender);
        final EditText password=findViewById(R.id.etPasswordSignUp);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        final EditText confirmpassword=findViewById(R.id.etConfirmPasswordSignUp);

        Log.e("TAGPass",password1+" "+confpassword);

            Log.e("TAGPass",password1+" "+confpassword);
                Button btnsignup = findViewById(R.id.btnSave);
                btnsignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email = emailid.getText().toString();
                        pass = password.getText().toString();
                        password1 = password.getText().toString();
                        confpassword = confirmpassword.getText().toString();
                        if(password1.length()!=confpassword.length())
                        {
                            Toast.makeText(SignUpActivity.this,"Confirm Password does not match the Password",Toast.LENGTH_SHORT).show();
                        }
                        else {

                            if(checkPassword(password1,confpassword)==1) {
                                firebaseAuth.createUserWithEmailAndPassword(email, pass)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();
                                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                                    String userId = firebaseUser.getUid();
                                                    String name = username.getText().toString();
                                                    Log.e("TAG", name + " ");
                                                    databaseReference = firebaseDatabase.getReference("Users").child(userId);

                                                    hashMap.put("id", userId);
                                                    hashMap.put("username", name);
                                                    hashMap.put("imgurl", "default");

                                                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent = new Intent(SignUpActivity.this, ChatListDisplayActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        }


    public  int checkPassword(String password1,String confpassword)
    {
        for(int i=0;i<password1.length();i++)
        {
            Log.e("TagPass",password1.charAt(i)+" "+confpassword.charAt(i));
            if(password1.charAt(i)!=confpassword.charAt(i))
            {
                Log.e("TagPass","returns 0");
                Toast.makeText(SignUpActivity.this,"Confirm Password does not match the Password",Toast.LENGTH_SHORT).show();
                return 0;
            }
        }
        return 1;
    }
    @Override
    protected void onStart() {
        super.onStart();
        hashMap.put("status","Offline");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_IMAGE&&resultCode==RESULT_OK&&data!=null&data.getData()!=null){
            profileimguri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),profileimguri);
                profilepic.setImageBitmap(bitmap);
                uploadImageToFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase() {
        StorageReference profilePicref=FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");

        if(profileimguri!=null)
        {
            profilePicref.putFile(profileimguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
