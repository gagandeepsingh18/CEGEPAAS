package com.example.cegepaas;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class AdvisorChatActivity extends AppCompatActivity {
    String advisorId,studentId,message;
    EditText text_message;
    ImageButton sendMessage;
    ChatAdapter chatAdapter;
    List<ChatPojo> chatList;
    RecyclerView recyclerView;
    DatabaseReference reference;

   

    private void sendMessage(String sender, String receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        reference.child("Chats").child(""+System.currentTimeMillis()/1000).setValue(hashMap);
    }

    private void readMessages(String sender, String receiver, String message){
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatPojo chat = snapshot.getValue(ChatPojo.class);
                    if((chat.getReceiver().equals(receiver) && chat.getSender().equals(sender)) ||
                            (chat.getReceiver().equals(sender) && chat.getSender().equals(receiver))){
                        chatList.add(chat);
                    }
                    recyclerView.setAdapter(new ChatAdapter(chatList,AdvisorChatActivity.this,advisorId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}