package org.uvigo.esei.unio.ui.services;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;
import org.uvigo.esei.unio.core.SQLManager;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.ui.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ServiceActivity extends AppCompatActivity {

    private static final String CURRENT_MSG = "CURRENT_MSG_";
    public static String SERVICE_NAME_KEY = "serviceName";

    private SQLManager sqlManager;

    private MessageAdapter msgAdapter;
    private List<Message> messages;
    private RecyclerView recyclerView;
    private String serviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        ActionBar actionBar = getSupportActionBar();

        serviceName = this.getIntent().getStringExtra(SERVICE_NAME_KEY);

        if (serviceName != null) {
            actionBar.setTitle(serviceName);
        }

        actionBar.setDisplayHomeAsUpEnabled(true);

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
            performService();
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

        if (serviceName != null) {
            String currentMsg
                    = SharedPreferencesManager.getString(this, CURRENT_MSG+serviceName);

            if (currentMsg != null) {
                ((EditText) findViewById(R.id.newMessage)).setText(currentMsg);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentMsg = ((EditText) findViewById(R.id.newMessage)).getText().toString();
        SharedPreferencesManager.setString(this, CURRENT_MSG+serviceName, currentMsg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String currentMsg = ((EditText) findViewById(R.id.newMessage)).getText().toString();
        SharedPreferencesManager.setString(this, CURRENT_MSG+serviceName, currentMsg);
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
        switch (item.getItemId()) {
            case R.id.option_clear:
                clearChat();
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

    protected void sendMessage(Message.Type msgType, String msg) {
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

        String newMessage;
        final EditText NEW_MESSAGE = this.findViewById(R.id.newMessage);
        newMessage = NEW_MESSAGE.getText().toString();

        if (!newMessage.equals("")) {
            NEW_MESSAGE.setText("");
            sendMessage(Message.Type.SENT_BY_USER, newMessage);
        }

        return newMessage;
    }

    protected void processUserInput() {
        performService();
    }

    protected void performService() {
        getAndSendUserInput();
    }

    protected void sendWelcomeMessage(String welcomeMsg) {
        String lastMsg = sqlManager.getLastMessage(getServiceName());
        if (!welcomeMsg.equals(lastMsg)) {
            sendMessage(welcomeMsg);
        }
    }


}