package com.example.lavisha.project;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> {

    Context context;
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
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgList=itemView.findViewById(R.id.imgUserList);
            usernameList=itemView.findViewById(R.id.tvusernameList);
        }
    }
}
