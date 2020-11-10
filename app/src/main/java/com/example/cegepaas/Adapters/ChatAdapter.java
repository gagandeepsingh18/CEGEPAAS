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

/**
 * ChatAdapter class is used to load the of chat details
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>
{
    public static final  int MSG_TYPE_LEFT = 0;
    public static final  int MSG_TYPE_RIGHT = 1;
    private List<ChatPojo> chatList;
    private Context context;
    private String sender;

    /**
     * Constructor used to get the details
     * @param chatList list of the chat
     * @param context context
     * @param sender sender information
     */
    public ChatAdapter(List<ChatPojo> chatList, Context context,String sender)
    {
        this.chatList = chatList;
        this.context = context;
        this.sender = sender;
    }

    /**
     * onCreateViewHolder to view the data
     * @param parent viewGroup
     * @param viewType viewType
     * @return view
     */
    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view;
        if(viewType == MSG_TYPE_LEFT)
        {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left,parent,false);
        }
        else
            {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right,parent,false);
            }
        return new ChatAdapter.ViewHolder(view);
    }

    /**
     * onBindViewHolder method
     * @param holder  ChatAdapter viewHolder item
     * @param position current position
     */
    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position)
    {
        ChatPojo chat = chatList.get(position);
        holder.show_message.setText(chat.getMessage());

    }

    /**
     * gets the item position
     * @param position current position
     * @return return the view type
     */
    @Override
    public int getItemViewType(int position)
    {

        if(chatList.get(position).getSender().equals(sender))
        {
            return  MSG_TYPE_RIGHT;
        }
        else
            {
            return MSG_TYPE_LEFT;
            }

    }

    /**
     * To get the count
     * @return returns the count.
     */
    @Override
    public int getItemCount()
    {
        return chatList.size();
    }

    /**
     * ViewHolder class for the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView show_message;

        /**
         * Constructor for the ViewHolder
         * @param itemView view object
         */
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
        }
    }
}
