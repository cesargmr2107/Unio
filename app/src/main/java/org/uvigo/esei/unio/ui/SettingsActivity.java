package org.uvigo.esei.unio.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.zip.Inflater;

public class SettingsActivity extends AppCompatActivity {

    public static final String CALC_PRECISION = "calculatorDecimalPrecision";
    public static final String TRANSLATE_SOURCE = "translationSourceLang";
    public static final String TRANSLATE_TRANSLATION = "translationTranslationLang";

    boolean preferenciasGuardadas;
    private static String password;
    String email;

    private NumberPicker calculatorNP;

    private Spinner sourceLangS;
    private Spinner translationLangS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle(R.string.settings);

        // Load buttons

        final Button WEATHER_BUTTON = this.findViewById(R.id.weather_settings);
        WEATHER_BUTTON.setOnClickListener(v -> {
            weatherSettings();
        });

        final Button MAIL_BUTTON = this.findViewById(R.id.mail_settings);
        MAIL_BUTTON.setOnClickListener(v -> {
            mailSettings();
        });

        final Button TRANSLATION_BUTTON = this.findViewById(R.id.translation_settings);
        TRANSLATION_BUTTON.setOnClickListener(v -> {
            translationSettings();
        });

        final Button CALCULATOR_BUTTON = this.findViewById(R.id.calculator_settings);
        CALCULATOR_BUTTON.setOnClickListener(v -> {
            calculatorSettings();
        });

    }

    private void mailSettings() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setView(R.layout.mail_settings);
        DLG.setPositiveButton("Save", (dialog, which) -> {
        });
        DLG.setNegativeButton("Cancel", null);
        DLG.create().show();
    }

    private void translationSettings() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setView(R.layout.translation_settings);
        DLG.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferencesManager.setString(this, TRANSLATE_SOURCE,
                                                sourceLangS.getSelectedItem().toString());

            SharedPreferencesManager.setString(this, TRANSLATE_TRANSLATION,
                                                translationLangS.getSelectedItem().toString());

            Toast.makeText(this, R.string.setting_saved, Toast.LENGTH_SHORT).show();
        });
        DLG.setNegativeButton("Cancel", null);
        AlertDialog alert = DLG.create();
        alert.show();
        sourceLangS = alert.findViewById(R.id.source_language_input);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                                     TranslationLanguages.getLanguageEngList());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLangS.setAdapter(arrayAdapter);
        String currentSourceLang = SharedPreferencesManager.getString(this, TRANSLATE_SOURCE);
        if (currentSourceLang == null) {
            currentSourceLang = TranslationManager.ORIGINAL_DEFAULT_SOURCE_LANG;
        }
        sourceLangS.setSelection(TranslationLanguages.getLanguageEngList().indexOf(currentSourceLang.toUpperCase()));

        translationLangS = alert.findViewById(R.id.translation_language_input);
        translationLangS.setAdapter(arrayAdapter);
        String currentTranslationLang = SharedPreferencesManager.getString(this, TRANSLATE_TRANSLATION);
        if (currentTranslationLang == null) {
            currentTranslationLang = TranslationManager.ORIGINAL_DEFAULT_TRANSLATION_LANG;
        }
        translationLangS.setSelection(TranslationLanguages.getLanguageEngList().indexOf(currentTranslationLang.toUpperCase()));
    }

    private void calculatorSettings() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setView(R.layout.calculator_settings);

        DLG.setNegativeButton("Cancel", null);
        DLG.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferencesManager.setInt(this, CALC_PRECISION, this.calculatorNP.getValue());
            Toast.makeText(this, R.string.setting_saved, Toast.LENGTH_SHORT).show();
        });
        AlertDialog alert = DLG.create();
        alert.show();
        calculatorNP = alert.findViewById(R.id.precision_input);
        calculatorNP.setMaxValue(15);
        calculatorNP.setMinValue(1);
        int currentValue = SharedPreferencesManager.getInt(this, CALC_PRECISION);
        if (currentValue != -1) {
            calculatorNP.setValue(currentValue);
        }
        else {
            calculatorNP.setValue(CalculatorManager.DEFAULT_PRECISION);
        }
        calculatorNP.setWrapSelectorWheel(true);
    }

    private void weatherSettings() {
        AlertDialog.Builder DLG = new AlertDialog.Builder(this);
        DLG.setTitle(R.string.temperature_unit);
        String[] options = {getString(R.string.celsius), getString(R.string.fahrenheit), getString(R.string.kelvin)};
        final boolean[] chosen = new boolean[3];
        DLG.setSingleChoiceItems(options, 0, (dialog, which) -> {
            chosen[which] = true;
        });
        DLG.setPositiveButton("Save", (dialog, which) -> {
        });
        DLG.setNegativeButton("Cancel", null);
        AlertDialog alert = DLG.create();
        alert.show();
        /*Spinner spinner = alert.findViewById(R.id.unit_input);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);*/
    }

/*
    @Override
    public void onPause(){
        super.onPause();
        try {
            guardarPreferencias();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            cargarPreferencias();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        }
        // para ver el funcionamiento, imprimimos preferencias si existen
        if (this.preferenciasGuardadas) {
            Toast.makeText(MainActivity.this, "Preferences were saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Preferences were not saved yet", Toast.LENGTH_SHORT).show();
        }
    }

    SecretKey secret = generateKey();
    byte[] passwd;
    //MODE_PRIVATE: sólo nuestra aplicación podrá leer y escribir datos de configuración en el fichero XML.
    //guardar configuración aplicación Android usando SharedPreferences
    public void guardarPreferencias() throws NoSuchPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException, InvalidKeySpecException {
        passwd= encryptMsg(password, secret);//llamo a encriptar y cojo el valor
        String pass = new String(passwd,
                StandardCharsets.UTF_8); //paso a String

        SharedPreferences prefs = getSharedPreferences("UnioPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("password", pass);
        editor.commit();
        Toast.makeText(this, "Saving preferences", Toast.LENGTH_SHORT).show();
    }

    public void cargarPreferencias() throws NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidParameterSpecException {
        SharedPreferences prefs = getSharedPreferences("UnioPreferences", Context.MODE_PRIVATE);
        String pass = decryptMsg(passwd, secret);
        this.email = prefs.getString("email", email);
        this.password = prefs.getString("password", pass);
        preferenciasGuardadas = prefs.getBoolean("preferenciasGuardadas", false);

    }
    public static SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeySpec secret;
        return secret = new SecretKeySpec(password.getBytes(), "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] cipherText = cipher.doFinal(message.getBytes("UTF-8"));
        return cipherText;
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException
    {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secret);
        String decryptString = new String(cipher.doFinal(cipherText), "UTF-8");
        return decryptString;
    }*/
}