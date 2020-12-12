package org.uvigo.esei.unio.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.SQLManager;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<ServiceData> servicios;

    private ArrayAdapter<ServiceData> chatListAdapter;
    private SQLManager sqlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        servicios = new ArrayList<>();

        servicios.add(new ServiceData("Traductor",
                                  "Servicio de traducción entre idiomas",
                                                  R.drawable.ic_baseline_translate,
                                                  TranslationServiceActivity.class));

        servicios.add(new ServiceData("El Tiempo",
                                  "Servicio de meteorología",
                                                  R.drawable.ic_baseline_cloud,
                                                  WeatherServiceActivity.class));

        servicios.add(new ServiceData("Correos",
                                  "Servicio de correo electrónico",
                                                  R.drawable.ic_baseline_mail,
                                                  MailServiceActivity.class));

        servicios.add(new ServiceData("Notas",
                                  "Servicio de bloc de notas",
                                                  R.drawable.ic_outline_sticky_note,
                                                  NotesServiceActivity.class));

        servicios.add(new ServiceData("Calculadora",
                                  "Servicio de calculadora",
                                                  R.drawable.ic_outline_calculate,
                                                  CalculatorServiceActivity.class));


        createChatList();

        sqlManager = new SQLManager(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean toRet = super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return toRet;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean toRet = super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.option_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.option_clear_all:
                AlertDialog.Builder builder = new AlertDialog.Builder( this );
                builder.setTitle("Remove all messages?");
                builder.setNegativeButton("Cancel", null);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sqlManager.deleteAllMessages();
                        Toast.makeText(MainActivity.this, "All messages deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
        }

        return toRet;
    }

    private void createChatList() {
        final ListView lvServices = this.findViewById(R.id.lv_chat_list);

        this.chatListAdapter = new ServiceDataArrayAdapter(this, this.servicios);
        lvServices.setAdapter(this.chatListAdapter);
    }

    public class ServiceData {
        private String serviceName;
        private String serviceDescription;
        private int serviceIcon;
        private Class serviceActivity;


        public ServiceData(String serviceName, String serviceDescription,
                           int serviceIcon, Class serviceActivity) {

            this.serviceName = serviceName;
            this.serviceDescription = serviceDescription;
            this.serviceIcon = serviceIcon;
            this.serviceActivity = serviceActivity;
        }

        public String getServiceName() {
            return serviceName;
        }

        public String getServiceDescription() {
            return serviceDescription;
        }

        public int getServiceIcon() {
            return this.serviceIcon;
        }

        public Class getServiceActivity() {
            return this.serviceActivity;
        }
    }
}
