package org.uvigo.esei.unio.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.ui.MainActivity;
import org.uvigo.esei.unio.ui.services.ServiceActivity;

import java.util.ArrayList;

public class ServiceDataArrayAdapter extends ArrayAdapter {
    public ServiceDataArrayAdapter(Context context, ArrayList<MainActivity.ServiceData> services) {
        super(context, 0, services);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Context CONTEXT = this.getContext();
        final LayoutInflater INFLATER = LayoutInflater.from(CONTEXT);
        final MainActivity.ServiceData SERVICE = (MainActivity.ServiceData) this.getItem(position);

        if (view == null) {
            view = INFLATER.inflate(R.layout.chat_list_item, null);
        }

        final TextView lblNombre = view.findViewById(R.id.tv_service_name);
        final TextView lblDescripcion = view.findViewById(R.id.tv_service_description);
        final ImageView ivIcono = view.findViewById(R.id.iv_service_icon);

        lblNombre.setText(SERVICE.getServiceName());
        lblDescripcion.setText(SERVICE.getServiceDescription());
        ivIcono.setBackgroundResource(SERVICE.getServiceIcon());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CONTEXT, SERVICE.getServiceActivity());
                intent.putExtra(ServiceActivity.SERVICE_NAME_KEY, SERVICE.getServiceName());

                CONTEXT.startActivity(intent);
            }
        });

        return view;
    }
}
