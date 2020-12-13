package org.uvigo.esei.unio.ui.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.Message;
import org.uvigo.esei.unio.core.SQLManager;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final SQLManager sqlManager;

    private Context context;
    private List<Message> messages;
    private final int MSG_TYPE_RIGHT = 0;
    private final int MSG_TYPE_LEFT = 1;

    public MessageAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        sqlManager = new SQLManager(context);
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
        holder.showMessage.setTag(message);
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

            itemView.findViewById(R.id.showMessage)
             .setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
                MenuItem Copy = contextMenu.add(Menu.NONE, 1, 1, "Copy");
                MenuItem Delete = contextMenu.add(Menu.NONE, 2, 2, "Delete");

                Copy.setOnMenuItemClickListener(menuItem -> {

                    ClipboardManager clipboard = (ClipboardManager) view.getContext()
                                                       .getSystemService(Context.CLIPBOARD_SERVICE);

                    clipboard.setPrimaryClip(
                            ClipData.newPlainText("Unio", showMessage.getText()));

                    Toast.makeText(view.getContext(),
                                    context.getString(R.string.msg_copied),
                                    Toast.LENGTH_SHORT).show();

                    return true;
                });

                Delete.setOnMenuItemClickListener(menuItem -> {

                    Message message = (Message) showMessage.getTag();

                    sqlManager.deleteMessage(message.getTableName(), message.getId());

                    Toast.makeText(view.getContext(),
                                "Mensaje eliminado",
                                    Toast.LENGTH_SHORT).show();

                    messages.remove(message);

                    MessageAdapter.this.notifyDataSetChanged();

                    return true;
                });
            });
        }
    }

}
