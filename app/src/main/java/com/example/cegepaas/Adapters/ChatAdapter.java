package com.example.cegepaas.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cegepaas.Model.ChatPojo;
import com.example.cegepaas.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    public static final  int MSG_TYPE_LEFT = 0;
    public static final  int MSG_TYPE_RIGHT = 1;
    private List<ChatPojo> chatList;
    private Context context;
    private String sender;

    public ChatAdapter(List<ChatPojo> chatList, Context context,String sender) {
        this.chatList = chatList;
        this.context = context;
        this.sender = sender;
    }


    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_LEFT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
        }
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        ChatPojo chat = chatList.get(position);
        holder.show_message.setText(chat.getMessage());

    }

    @Override
    public int getItemViewType(int position) {

        if(chatList.get(position).getSender().equals(sender)){
            return  MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LEFT;
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }
}
