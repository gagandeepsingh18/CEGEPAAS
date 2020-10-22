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
    String advisorId,message,studentId;
    ImageButton sendMessage;
    EditText textMessage;
    ChatAdapter chatAdapter;
    List<ChatPojo> chatList;
    RecyclerView recyclerView;
    DatabaseReference reference;
    @Override
   