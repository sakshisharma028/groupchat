package com.example.lavisha.project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth=FirebaseAuth.getInstance();
        final EditText etUserName=findViewById(R.id.etEmailOrPhone);
        final EditText etPassword=findViewById(R.id.etPassword);
        TextView tvForgotPassword=findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=etUserName.getText().toString();
                FirebaseAuth.getInstance().sendPasswordResetEmail(username)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Email sent",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        Button btnSignIn=findViewById(R.id.btnSignIn);
        Button btnSignUp=findViewById(R.id.btnCreateANewAccount);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=etUserName.getText().toString();
                password=etPassword.getText().toString();
                firebaseAuth.signInWithEmailAndPassword(username,password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this,"Logged In Successfully",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(MainActivity.this,ChatListDisplayActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                                   finish();
                                }
                                else{
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Log.d("TAGFinal","onStart of mainavtivity");
            Intent intent=new Intent(MainActivity.this,ChatListDisplayActivity.class);
            startActivity(intent);
        }
    }



}
