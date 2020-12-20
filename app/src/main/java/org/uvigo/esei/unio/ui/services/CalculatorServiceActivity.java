package org.uvigo.esei.unio.ui.services;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.CalculatorManager;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.ui.SettingsActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculatorServiceActivity extends InternetServiceActivity {

    public static final String CALC_PRECISION = "calculatorDecimalPrecision";
    private int precision;
    private NumberPicker calculatorNP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.calculator_welcome));

        updateSettings(this);
    }

    @Override
    protected void performService() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {

            String result;
            try {
                result = CalculatorManager.calculate(newMessage, precision);
                sendMessage(result);
            } catch (CalculatorManager.CalculatorManagerException e) {
                result = getString(R.string.calculator_fail);
                sendMessage(result);
            }
        }
    }


    @Override
    public void settings(Context context){
        AlertDialog.Builder DLG = new AlertDialog.Builder(context);
        DLG.setView(R.layout.calculator_settings);

        DLG.setNegativeButton("Cancel", null);
        DLG.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferencesManager.setInt(context, CALC_PRECISION, calculatorNP.getValue());
            Toast.makeText(context, R.string.setting_saved, Toast.LENGTH_SHORT).show();
            updateSettings(context);
        });
        AlertDialog alert = DLG.create();
        alert.show();
        calculatorNP = alert.findViewById(R.id.precision_input);
        calculatorNP.setMaxValue(15);
        calculatorNP.setMinValue(1);
        int currentValue = SharedPreferencesManager.getInt(context, CALC_PRECISION);
        if (currentValue != -1) {
            calculatorNP.setValue(currentValue);
        } else {
            calculatorNP.setValue(CalculatorManager.DEFAULT_PRECISION);
        }
        calculatorNP.setWrapSelectorWheel(true);
    }

    @Override
    protected void updateSettings(Context context) {
        precision = SharedPreferencesManager.getInt(context, CALC_PRECISION);

        if (precision == -1) {
            precision = CalculatorManager.DEFAULT_PRECISION;
        }
    }

}
