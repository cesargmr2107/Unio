package org.uvigo.esei.unio.core;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class CalculatorManager {

    private static final String BASE_URL = "https://api.mathjs.org/v4/?expr=%s";
    private static final String LOG_TAG = "CalculatorManager";

    public static String calculate(String input) throws CalculatorManagerException {
        String toRet = null;

        try {
            String url = String.format(BASE_URL, URLEncoder.encode(input, "utf-8"));

            System.out.println("URL: " + url);

            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(1000 /* millisegundos */);
            connection.setConnectTimeout(1000 /* millisegundos */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            connection.connect();
            int codigoRespuesta = connection.getResponseCode();
            InputStream is = connection.getInputStream();

            String response = getStringFromStream(is);

            toRet = response;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        if (toRet == null) {
            throw new CalculatorManager.CalculatorManagerException();
        }

        return toRet;
    }

    private static String getStringFromStream(InputStream is) {
        StringBuilder toret = new StringBuilder();
        String line = "";
        try ( BufferedReader reader = new BufferedReader( new InputStreamReader( is ) ) ) {
            while( ( line = reader.readLine() ) != null ) {
                toret.append( line );
            }
        } catch (IOException e) {
            Log.e( LOG_TAG, " in getStringFromString(): error converting net input to string" );
        }
        return toret.toString();
    }

    public static class CalculatorManagerException extends Exception {
        public CalculatorManagerException() {
            super();
        }
    }

}
