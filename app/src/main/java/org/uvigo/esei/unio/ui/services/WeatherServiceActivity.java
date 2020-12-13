package org.uvigo.esei.unio.ui.services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.WeatherManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherServiceActivity extends InternetServiceActivity {

    WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherManager = new WeatherManager();
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
                String weatherString = getString(R.string.weather_info,
                        weatherInfo.getCity(),
                        weatherInfo.getDescription(),
                        weatherInfo.getTemperature(),
                        weatherInfo.getFeelsLike(),
                        weatherInfo.getMinTemp(),
                        weatherInfo.getMaxTemp(),
                        weatherInfo.getHumidity());
                sendMessage(weatherString);
            } catch (WeatherManager.WeatherManagerException exception) {
                sendMessage(getString(R.string.city_not_exist, city));
            }
        }
    }
}