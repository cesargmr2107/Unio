package org.uvigo.esei.unio.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static final String UNIO_PREFERENCES = "UnioPreferences";

    public static void setString(Context cntxt, String key, String value) {
        SharedPreferences.Editor editor = getEditor(cntxt);
        editor.putString(key, value);
        editor.commit();
    }

    public static void setInt(Context cntxt, String key, int value) {
        SharedPreferences.Editor editor = getEditor(cntxt);
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(Context cntxt, String key) {
        return getSharedPreferences(cntxt).getString(key, null);
    }

    public static int getInt(Context cntxt, String key) {
        return getSharedPreferences(cntxt).getInt(key, -1);
    }

    private static SharedPreferences getSharedPreferences(Context cntxt) {
        return cntxt.getSharedPreferences(UNIO_PREFERENCES, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context cntxt) {
        return getSharedPreferences(cntxt).edit();
    }
}
