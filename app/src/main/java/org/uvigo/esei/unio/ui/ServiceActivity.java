package org.uvigo.esei.unio.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;

import java.util.ArrayList;
import java.util.List;

public class ServiceActivity extends AppCompatActivity {

    private MessageAdapter msgAdapter;
    private List<Message> messages;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        messages = new ArrayList<>();
        msgAdapter = new MessageAdapter(ServiceActivity.this, messages);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(ServiceActivity.this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(msgAdapter);

        final ImageButton SEND_BT = this.findViewById(R.id.sendBt);
        SEND_BT.setOnClickListener(v -> {
            processUserInput();
        });

    }

    private void sendMessage(Message.Type msgType, String msg) {
        messages.add(new Message(msgType, msg));
        recyclerView.smoothScrollToPosition(messages.size());
        msgAdapter.notifyDataSetChanged();
    }

    protected void sendMessage(String msg){
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

}