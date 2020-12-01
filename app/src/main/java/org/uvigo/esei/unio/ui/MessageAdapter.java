package org.uvigo.esei.unio.ui;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private Context context;
    private List<Message> messages;
    private final int MSG_TYPE_RIGHT = 0;
    private final int MSG_TYPE_LEFT = 1;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
        }
        return new MessageAdapter.ViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (message.getType().equals(Message.Type.SENT_BY_SYSTEM)) {
            holder.showMessage.setText(Html.fromHtml(message.getText(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        } else {
            holder.showMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getType().equals(Message.Type.SENT_BY_USER)) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView showMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            showMessage = itemView.findViewById(R.id.showMessage);
        }
    }

}
