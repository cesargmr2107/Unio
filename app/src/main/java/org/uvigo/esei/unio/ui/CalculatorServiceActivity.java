package org.uvigo.esei.unio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.CalculatorManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CalculatorServiceActivity extends ServiceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.sendWelcomeMessage(getString(R.string.calculator_welcome));
    }

    @Override
    protected void processUserInput() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {
            final Executor EXECUTOR = Executors.newSingleThreadExecutor();
            final Handler HANDLER = new Handler(Looper.getMainLooper());

            EXECUTOR.execute( () -> {
                String result;
                try {
                    result = CalculatorManager.calculate(newMessage);
                } catch (CalculatorManager.CalculatorManagerException e) {
                    result = getString(R.string.calculator_fail);

                    e.printStackTrace();
                }

                String finalResult = result;
                HANDLER.post( () -> {
                   sendMessage(finalResult);
                });
            });
            /*
            try {
                String result = CalculatorManager.calculate(newMessage);

                sendMessage(result);
            } catch (CalculatorManager.CalculatorManagerException e) {
                sendMessage(getString(R.string.calculator_fail));

                e.printStackTrace();
            }
            */
        }
    }
}
