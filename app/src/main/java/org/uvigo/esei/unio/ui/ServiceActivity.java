package org.uvigo.esei.unio.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;
import org.uvigo.esei.unio.core.SQLManager;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private SQLManager sqlManager;

    private MessageAdapter msgAdapter;
    private List<Message> messages;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        //this.getApplicationContext().deleteDatabase("ServiceMessages");
        sqlManager = new SQLManager(this.getApplicationContext());

        messages = new ArrayList<>();
        msgAdapter = new MessageAdapter(ServiceActivity.this, messages);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(msgAdapter);

        final ImageButton SEND_BT = this.findViewById(R.id.sendBt);
        SEND_BT.setOnClickListener(v -> {
            processUserInput();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Message> dbMessages = sqlManager.getMessages(getServiceName());
        messages.clear();
        messages.addAll(dbMessages);
        msgAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messages.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean toRet = super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.chat_menu, menu);
        return toRet;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean toRet = super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.option_clear: clearChat();
                break;
        }
        return toRet;
    }

    private void clearChat() {
        messages.clear();
        msgAdapter.notifyDataSetChanged();
        sqlManager.deleteAllMessages(getServiceName());
    }

    private String getServiceName() {
        String wholeName = this.getClass().getSimpleName();
        return wholeName.substring(0, wholeName.length() - 8);
    }

    private void sendMessage(Message.Type msgType, String msg) {
        Message newMessage = new Message(msgType, msg);
        messages.add(newMessage);
        recyclerView.smoothScrollToPosition(messages.size());
        msgAdapter.notifyDataSetChanged();
        sqlManager.addMessage(getServiceName(), newMessage);
    }

    protected void sendMessage(String msg) {
        sendMessage(Message.Type.SENT_BY_SYSTEM, msg);
    }

    protected String getAndSendUserInput() {
        final EditText NEW_MESSAGE = this.findViewById(R.id.newMessage);
        String newMessage = NEW_MESSAGE.getText().toString();
        if (!newMessage.equals("")) {
            NEW_MESSAGE.setText("");
            sendMessage(Message.Type.SENT_BY_USER, newMessage);
        }
        return newMessage;
    }

    protected void processUserInput() {
        getAndSendUserInput();
    }

    protected void sendWelcomeMessage(String welcomeMsg){
        String lastMsg = sqlManager.getLastMessage(getServiceName());
        if(!welcomeMsg.equals(lastMsg)){
            sendMessage(welcomeMsg);
        }
    }
}