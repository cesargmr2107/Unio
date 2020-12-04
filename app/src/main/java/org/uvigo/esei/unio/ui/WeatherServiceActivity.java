package org.uvigo.esei.unio.ui;

import android.os.Bundle;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.WeatherManager;

public class WeatherServiceActivity extends ServiceActivity {

    WeatherManager weatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherManager = new WeatherManager();
        super.sendWelcomeMessage("If you tell me the name of a city at any time, " +
                "I'll show its weather forecast for today");
    }

    @Override
    protected void processUserInput() {
        String city = super.getAndSendUserInput().trim();
        final String regex = "([A-Za-zNñ])+";
        if (!city.matches(regex)) {
            super.sendMessage("That's not a proper city name!");
        } else {
            try {
                WeatherManager.WeatherInfo weatherInfo = weatherManager.getWeatherInfo(city);
                String weatherString = "Current weather in " + weatherInfo.getCity() + ": <br/>" +
                        "· Description: " + weatherInfo.getDescription() + "<br/>" +
                        "· Temperature: " + weatherInfo.getTemperature() + "<br/>" +
                        "· Feels like: " + weatherInfo.getFeelsLike() + "<br/>" +
                        "· Minimum temperature: " + weatherInfo.getMinTemp() + "<br/>" +
                        "· Maximum temperature: " + weatherInfo.getMaxTemp() + "<br/>" +
                        "· Humidity: " + weatherInfo.getHumidity();
                sendMessage(weatherString);
            } catch (WeatherManager.WeatherManagerException exception) {
                sendMessage("Sorry, I didn't find the weather for " + city);
            }
        }
    }
}