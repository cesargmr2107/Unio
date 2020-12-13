package org.uvigo.esei.unio.ui.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.CalculatorManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculatorServiceActivity extends InternetServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.calculator_welcome));
    }

    @Override
    protected void performService() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {

            String result;
            try {
                result = CalculatorManager.calculate(newMessage);
                sendMessage(result);
            } catch (CalculatorManager.CalculatorManagerException e) {
                result = getString(R.string.calculator_fail);
                e.printStackTrace();
            }
        }
    }

}
