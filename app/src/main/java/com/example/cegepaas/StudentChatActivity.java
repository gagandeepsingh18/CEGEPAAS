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

import com.example.cegepaas.Adapters.ChatAdapter;
import com.example.cegepaas.Model.ChatPojo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentChatActivity extends AppCompatActivity {
    String advisorId, message, studentId;
    ImageButton sendMessage;
    EditText textMessage;
    ChatAdapter chatAdapter;
    List<ChatPojo> chatList;
    RecyclerView recyclerView;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        advisorId = getIntent().getStringExtra("uname");
        textMessage = findViewById(R.id.text_send);
        sendMessage = findViewById(R.id.btn_send);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        SharedPreferences sp = getSharedPreferences("AA", 0);
        studentId = sp.getString("suname", "-");
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = textMessage.getText().toString();
                if (!message.equals("")) {
                    sendMessage(studentId, advisorId, message);
                } else {
                    Toast.makeText(StudentChatActivity.this, "Can't send empty message..", Toast.LENGTH_SHORT).show();
                }
                textMessage.setText("");
            }
        });
        readMessages(studentId, advisorId, message);
    }

    private void sendMessage(String sender, String receiver, String message) {
        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        reference.child("Chats").child("" + System.currentTimeMillis() / 1000).setValue(hashMap);
    }

    private void readMessages(String sender, String receiver, String message) {
        chatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatPojo chat = snapshot.getValue(ChatPojo.class);
                    if ((chat.getReceiver().equals(receiver) && chat.getSender().equals(sender)) ||
                            (chat.getReceiver().equals(sender) && chat.getSender().equals(receiver))) {
                        chatList.add(chat);
                    }
                    recyclerView.setAdapter(new ChatAdapter(chatList, StudentChatActivity.this, studentId));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
