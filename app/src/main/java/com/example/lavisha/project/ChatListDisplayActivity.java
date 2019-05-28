package com.example.lavisha.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatListDisplayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseUser firebaseUser;
    DatabaseReference dbref;
    User user=new User();
    ArrayList<User>arrayList=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat_list_display_activity);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        dbref=FirebaseDatabase.getInstance().getReference("Users");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    User user=dataSnapshot1.getValue(User.class);
                    Log.e("TAG",user.getUsername()+" name");
                    assert user != null;
                    if(!user.getId().equals(firebaseUser.getUid())) {
                        arrayList.add(user);
                    }
                }

               ChatListAdapter chatListAdapter=new ChatListAdapter(ChatListDisplayActivity.this,arrayList);
                recyclerView.setAdapter(chatListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.menuLogOut:
                firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

                dbref=FirebaseDatabase.getInstance().getReference("Users");
                Log.e("TAGFinal","uid"+ firebaseUser.getUid());

                dbref.child(firebaseUser.getUid()).child("status").setValue("Offline");
                FirebaseAuth.getInstance().signOut();
             //   Log.d("TAGFinal",firebaseUser+"");
                finish();
                startActivity(new Intent(ChatListDisplayActivity.this,MainActivity.class));
                return true;

            case R.id.menuEditProfile:
                Intent intent=new Intent(ChatListDisplayActivity.this,EditProfile.class);
                startActivity(intent);
        }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Log.e("TAG3","onResume displaylist");

        dbref=FirebaseDatabase.getInstance().getReference("Users");
        //Log.e("TAGABC","Id is:"+id);
        dbref.child(firebaseUser.getUid()).child("status").setValue("Online");
    }

    @Override
    protected void onPause() {

        Log.d("TAGFinal","in OnStop");
        super.onPause();
        Log.e("TAG3","onstop displaylist");
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            dbref = FirebaseDatabase.getInstance().getReference("Users");
//        Log.e("TAGFinal","uid"+ firebaseUser.getUid());


            dbref.child(firebaseUser.getUid()).child("status").setValue("Offline");
        }
        finish();
    }
}
