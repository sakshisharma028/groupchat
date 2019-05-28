package com.example.lavisha.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
    String last_message;
    ArrayList<User>userArrayList;

    public ChatListAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final User user=userArrayList.get(i);
        viewHolder.usernameList.setText(user.getUsername());
        if(user.getDp()==null)
        {
            viewHolder.imgList.setImageResource(R.drawable.ic_default_user_dp);
        }else {
            Picasso.get().load(user.getDp()).into(viewHolder.imgList);

        }
        lastMessage(user.getId(),viewHolder.tvlastMessage);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("UserId",user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgList;
        TextView usernameList;
        TextView tvlastMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgList=itemView.findViewById(R.id.imgUserList);
            usernameList=itemView.findViewById(R.id.tvusernameList);
            tvlastMessage=itemView.findViewById(R.id.tvlastMessage);
        }
    }

    public void lastMessage(final String userId, final TextView tvlastmes)
    {
        last_message="default";
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Chat chat=snapshot.getValue(Chat.class);
                    if (user != null && chat != null && (chat.getSender().equals(userId) && chat.getReceiver().equals(user.getUid()) || chat.getSender().equals(user.getUid())
                            && chat.getReceiver().equals(userId))) {
                        last_message = chat.getMessage();
                    }
                }

                switch (last_message)
                {
                    case "default":
                        tvlastmes.setText("");
                        break;
                        default:
                            tvlastmes.setText(last_message);
                            break;
                }
              //  Log.e("TAG4",last_message);
                last_message="default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
