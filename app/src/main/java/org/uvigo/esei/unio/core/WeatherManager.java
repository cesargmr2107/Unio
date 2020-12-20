package org.uvigo.esei.unio.core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeatherManager {

    private final static String APP_ID = "10f39079bb4dfeb26f98a0daba4421d0";

    public enum Unit{metric, imperial, standard}
    private String unit;

    public WeatherManager(String unit){
        this.unit = unit;
    }

    public WeatherInfo getWeatherInfo(String city) throws WeatherManagerException {
        String forecastJsonString = getJSONWeatherInfo(city);
        WeatherInfo weatherInfo = processWeatherInfo(forecastJsonString, city);

        if (weatherInfo == null) {
            throw new WeatherManagerException();
        }

        return weatherInfo;
    }

    private String getJSONWeatherInfo(String city) {

        String forecastJson = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city +
                    "&appid=" + APP_ID +
                    "&units=" + unit;
            URL url = new URL(urlString);

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                forecastJson = "";
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                forecastJson = "";
            }

            forecastJson = buffer.toString();

        } catch (Exception exception) {
            forecastJson = "";
            Log.e("weather-fetching-error", "ERROR: " + exception.getMessage());
        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("weather-fetching-error", "Error closing stream", e);
                }
            }

            return forecastJson;
        }
    }

    private WeatherInfo processWeatherInfo(String forecastJsonString, String city) {
        WeatherInfo weatherInfo = null;
        try {
            JSONObject forecastJson = new JSONObject(forecastJsonString);
            int code = forecastJson.getInt("cod");

            if (code == 200) {

                JSONArray weatherArray = forecastJson.getJSONArray("weather");
                String description = weatherArray.getJSONObject(0).getString("description");

                JSONObject mainFields = forecastJson.getJSONObject("main");
                String feelsLike = mainFields.getString("feels_like");
                String temperature = mainFields.getString("temp");
                String maxTemp = mainFields.getString("temp_max");
                String minTemp = mainFields.getString("temp_min");
                String humidity = mainFields.getString("humidity");

                weatherInfo = new WeatherInfo(city, description, feelsLike, temperature, maxTemp, minTemp, humidity);
            }

        } catch (Exception exception) {
            Log.e("weather-parsing-error", "ERROR: " + exception.getMessage());
            weatherInfo = null;
        }

        return weatherInfo;
    }

    public class WeatherManagerException extends Exception {
        public WeatherManagerException() {
            super();
        }
    }

    public class WeatherInfo {

        String city;
        String description;
        String feelsLike;
        String temperature;
        String maxTemp;
        String minTemp;
        String humidity;

        public WeatherInfo(String city, String description, String feelsLike, String temperature, String maxTemp, String minTemp, String humidity) {
            this.city = city;
            this.description = description;
            this.feelsLike = feelsLike;
            this.temperature = temperature;
            this.maxTemp = maxTemp;
            this.minTemp = minTemp;
            this.humidity = humidity;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFeelsLike() {
            return feelsLike;
        }

        public void setFeelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
        }

        public String getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(String minTemp) {
            this.minTemp = minTemp;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }
    }

}
