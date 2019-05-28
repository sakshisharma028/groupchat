package com.example.lavisha.project;

import android.content.Context;
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

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Chat>chatList;
    FirebaseUser user;
    public MessageAdapter(Context context, ArrayList<Chat> chatList) {
        this.context = context;
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(i==1)
        {
            Log.e("TAG2","in 1");
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itwm_send_chat,viewGroup,false);
            return new ViewHolder1(view);
        }
        else{
            Log.e("TAG2","in2");
            View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_receive_chat,viewGroup,false);
            return new ViewHolder2(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Chat chat=chatList.get(i);
        if(getItemViewType(i)==1)
        {
            Log.e("TAGNEW","In onbind1");
            ViewHolder1 viewHolder1= (ViewHolder1) viewHolder;
            viewHolder1.messageOfUser.setText(chat.getMessage());
            Log.e("TAGNEW",chat.getisSeen()+" seen");
            if(chat.getisSeen())
            {
                viewHolder1.seenornot.setText("Seen");
            }
            else{
                viewHolder1.seenornot.setText("Delivered");
            }
            viewHolder1.time.setText(chat.getTime());
        }
        else if(getItemViewType(i)==0){
            Log.e("TAGNEW","In onbind2");
            ViewHolder2 viewHolder2= (ViewHolder2) viewHolder;
            viewHolder2.messageOfUser1.setText(chat.getMessage());
            viewHolder2.time.setText(chat.getTime());
        }
    }


    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        TextView messageOfUser;
        TextView seenornot;
        TextView time;

                public ViewHolder1(@NonNull View itemView) {
                    super(itemView);
                    time=itemView.findViewById(R.id.tvTime);
                    seenornot=itemView.findViewById(R.id.tvSeenOrNot);
                    messageOfUser=itemView.findViewById(R.id.messageUserMessagesend);
                }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView messageOfUser1;
        TextView time;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            time=itemView.findViewById(R.id.tvTime);
            messageOfUser1=itemView.findViewById(R.id.messageUserMessagerec);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(user.getUid())) {
            Log.e("TAG2","in getItem1");
            return 1;
        } else  {
            Log.e("TAG2","in getItem0");
            return 0;
        }
    }

}
