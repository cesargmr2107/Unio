package org.uvigo.esei.unio.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.WeatherManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeatherServiceActivity extends ServiceActivity {

    WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherManager = new WeatherManager();
        super.sendWelcomeMessage(getString(R.string.weather_welcome));
    }

    @Override
    protected void processUserInput() {
        String city = super.getAndSendUserInput().trim();
        final String regex = "([A-Za-zNñ])+";

        final Executor EXECUTOR = Executors.newSingleThreadExecutor();
        final Handler HANDLER = new Handler(Looper.getMainLooper());

        EXECUTOR.execute( () -> {
            if (!city.matches(regex)) {
                HANDLER.post( () -> {super.sendMessage(getString(R.string.no_letters_weather));});
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

                    HANDLER.post( () -> {sendMessage(weatherString);});
                } catch (WeatherManager.WeatherManagerException exception) {

                    HANDLER.post( () -> {sendMessage(getString(R.string.city_not_exist, city));});
                }
            }
        });
    }
}