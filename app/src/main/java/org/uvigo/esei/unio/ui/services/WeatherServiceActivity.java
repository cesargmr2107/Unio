package org.uvigo.esei.unio.ui.services;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.core.WeatherManager;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherServiceActivity extends InternetServiceActivity {

    private static final String UNITS_WEATHER = "specificUnits";
    private String unit;
    private WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateSettings(this);
        super.sendWelcomeMessage(getString(R.string.weather_welcome));
    }

    @Override
    protected void performService() {
        String city = super.getAndSendUserInput().trim();
        final String regex = "([A-Za-zNñ])+";

        if (!city.matches(regex)) {
            sendMessage(getString(R.string.no_letters_weather));
        } else {
            try {
                WeatherManager.WeatherInfo weatherInfo = weatherManager.getWeatherInfo(city);

                String symbol = "";

                switch (unit) {
                    case "metric":
                        symbol = "°C";
                        break;
                    case "imperial":
                        symbol = "°F";
                        break;
                    case "standard":
                        symbol = "K";
                        break;
                }

                String weatherString = getString(R.string.weather_info,
                        weatherInfo.getCity(),
                        weatherInfo.getDescription(),
                        weatherInfo.getTemperature(),
                        weatherInfo.getFeelsLike(),
                        weatherInfo.getMinTemp(),
                        weatherInfo.getMaxTemp(),
                        weatherInfo.getHumidity(),
                        symbol);

                sendMessage(weatherString);
            } catch (WeatherManager.WeatherManagerException exception) {
                sendMessage(getString(R.string.city_not_exist, city));
            }
        }
    }

    @Override
    public void settings(Context context) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(context);
        DLG.setTitle(R.string.temperature_unit);
        String[] options = {context.getString(R.string.celsius), context.getString(R.string.fahrenheit), context.getString(R.string.kelvin)};
        final int[] unit = new int[1];
        String chosen = SharedPreferencesManager.getString(context, UNITS_WEATHER);
        int chosenIndex = 0;
        if (chosen != null) {
            chosenIndex = WeatherManager.Unit.valueOf(chosen).ordinal();
        }
        DLG.setSingleChoiceItems(options, chosenIndex, (dialog, which) -> {
            unit[0] = which;
        });
        DLG.setPositiveButton(R.string.save, (dialog, which) -> {
            SharedPreferencesManager.setString(context, UNITS_WEATHER, WeatherManager.Unit.values()[unit[0]].toString());
            Toast.makeText(context, R.string.setting_saved, Toast.LENGTH_SHORT).show();
            updateSettings(context);
        });
        DLG.setNegativeButton(R.string.cancel, null);
        AlertDialog alert = DLG.create();
        alert.show();
    }

    @Override
    protected void updateSettings(Context context) {
        unit = SharedPreferencesManager.getString(context, UNITS_WEATHER);
        if (unit == null) {
            unit = WeatherManager.Unit.metric.toString();
        }
        weatherManager = new WeatherManager(unit);
    }
}