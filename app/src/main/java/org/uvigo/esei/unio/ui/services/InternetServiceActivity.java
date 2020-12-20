package org.uvigo.esei.unio.ui.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;
import org.uvigo.esei.unio.core.SQLManager;
import org.uvigo.esei.unio.ui.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InternetServiceActivity extends ServiceActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler(this.getMainLooper());
        final ImageButton SEND_BT = this.findViewById(R.id.sendBt);
        SEND_BT.setOnClickListener(v -> {
            if (hasConnection()) {
                final Executor EXECUTOR = Executors.newSingleThreadExecutor();
                EXECUTOR.execute(() -> {
                    performService();
                });
            } else {
                super.getAndSendUserInput();
                sendMessage(getString(R.string.no_internet_connection));
            }
        });
    }


    private boolean hasConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) InternetServiceActivity.this
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) {
            return false;
        }
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }

    @Override
    protected void sendMessage(String msg) {
        handler.post(() -> {
            super.sendMessage(msg);
        });
    }

    @Override
    protected String getAndSendUserInput() {

        String[] newMessage = new String[1];

        handler.post(() -> {
            final EditText NEW_MESSAGE = this.findViewById(R.id.newMessage);
            String msg = NEW_MESSAGE.getText().toString();
            if (!msg.equals("")) {
                NEW_MESSAGE.setText("");
                super.sendMessage(Message.Type.SENT_BY_USER, msg);
            }
            newMessage[0] = msg;
            synchronized (newMessage) {
                newMessage.notify();
            }
        });

        try {
            synchronized (newMessage) {
                newMessage.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return newMessage[0];
    }

    @Override
    public void settings(Context context) {

    }

    @Override
    protected void updateSettings(Context context) {

    }

}