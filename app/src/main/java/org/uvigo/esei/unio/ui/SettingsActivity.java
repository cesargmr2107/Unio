package org.uvigo.esei.unio.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.CalculatorManager;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.core.TranslationLanguages;
import org.uvigo.esei.unio.core.TranslationManager;
import org.uvigo.esei.unio.ui.services.CalculatorServiceActivity;
import org.uvigo.esei.unio.ui.services.MailServiceActivity;
import org.uvigo.esei.unio.ui.services.TranslationServiceActivity;
import org.uvigo.esei.unio.ui.services.WeatherServiceActivity;

import java.util.ArrayList;
import java.util.zip.Inflater;

import javax.microedition.khronos.egl.EGLDisplay;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.settings);

        // Load buttons

        final Button WEATHER_BUTTON = this.findViewById(R.id.weather_settings);
        WEATHER_BUTTON.setOnClickListener(v -> {
            new WeatherServiceActivity().settings(this);
        });

        final Button MAIL_BUTTON = this.findViewById(R.id.mail_settings);
        MAIL_BUTTON.setOnClickListener(v -> {
            new MailServiceActivity().settings(this);
        });

        final Button TRANSLATION_BUTTON = this.findViewById(R.id.translation_settings);
        TRANSLATION_BUTTON.setOnClickListener(v -> {
            new TranslationServiceActivity().settings(this);
        });

        final Button CALCULATOR_BUTTON = this.findViewById(R.id.calculator_settings);
        CALCULATOR_BUTTON.setOnClickListener(v -> {
            new CalculatorServiceActivity().settings(this);
        });

    }

}