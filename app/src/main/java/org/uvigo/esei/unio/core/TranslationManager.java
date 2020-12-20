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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class TranslationManager {

    private final String BASE_URL = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=%s&tl=%s&dt=t&q=%s&ie=UTF-8&oe=UTF-8";
    private final String LOG_TAG = "TranslationManager";

    public static String ORIGINAL_DEFAULT_SOURCE_LANG = "spanish";
    public static String ORIGINAL_DEFAULT_TRANSLATION_LANG = "english";

    private String defaultOriginalLanguage;
    private String defaultTranslationLanguage;

    public TranslationManager(String defaultOriginalLanguage, String defaultTranslationLanguage) {
        this.defaultOriginalLanguage = defaultOriginalLanguage;
        this.defaultTranslationLanguage = defaultTranslationLanguage;
    }

    public void setDefaultOriginalLanguage(String defaultOriginalLanguage) {
        this.defaultOriginalLanguage = defaultOriginalLanguage;
    }

    public void setDefaultTranslationLanguage(String defaultTranslationLanguage) {
        this.defaultTranslationLanguage = defaultTranslationLanguage;
    }

    public String translate(String originalText) throws TranslationManagerException {
        return translate(originalText, defaultOriginalLanguage, defaultTranslationLanguage);
    }

    public String translate(String originalText, String fromLang, String toLang)
                            throws TranslationManagerException {

        boolean success = true;
        StringBuilder translation = new StringBuilder();

        try {
            String url = String.format(BASE_URL, fromLang, toLang,
                    URLEncoder.encode(originalText, "utf-8"));

            System.out.println("URL: " + url);

            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(1000 /* millisegundos */);
            connection.setConnectTimeout(1000 /* millisegundos */);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            connection.connect();
            //int codigoRespuesta = connection.getResponseCode();
            InputStream is = connection.getInputStream();

            JSONArray json = new JSONArray(getStringFromStream(is)).getJSONArray(0);

            for (int i = 0; i < json.length(); i++) {
                translation.append(json.getJSONArray(i).getString(0).replaceAll("\\n", "<br/>"));
            }
        }
        catch (IOException | JSONException e) {
            success = false;
        }

        String result;

        if (success) {
            result = translation.toString();
        }
        else {
            throw new TranslationManagerException();
        }

        return result;
    }

    private String getStringFromStream(InputStream is) {
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

    public class TranslationManagerException extends Exception {
        public TranslationManagerException() {
            super();
        }
    }
}
