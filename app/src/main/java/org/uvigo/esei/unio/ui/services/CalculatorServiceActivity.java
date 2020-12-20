package org.uvigo.esei.unio.ui.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.CalculatorManager;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.ui.SettingsActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculatorServiceActivity extends InternetServiceActivity {
    private int precision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.calculator_welcome));

        precision = SharedPreferencesManager.getInt(this, SettingsActivity.CALC_PRECISION);

        if (precision == -1) {
            precision = CalculatorManager.DEFAULT_PRECISION;
        }
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
                e.printStackTrace();
            }
        }
    }

}
