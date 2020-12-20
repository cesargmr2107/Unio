package org.uvigo.esei.unio.core;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesManager {

    public static final String UNIO_PREFERENCES = "UnioPreferences";
    private static final String SECRET_KEY = "EK@>@rWqYguTSMP4TTGT9:#&3{8w3CWR'>qxh&}U\"QdDdY{W-BS`HpK7Y*+EN9CXscY}cC&{7qhUP!5Qxx2hH;Lt6:-b#e#P#`KS,\\%*a&yW,aQ5LTchUWVP6LD.]^PxZ$4ScyVzgS+<%uBMqCfq[X#G9[sAgkXTdau<.X!R4ghp4]CPXU,j.??Bv9\\g>4m^a82Dm*:Z^Leq2g#.8<,<c>bGPTP&^]--XRUeBr'6KK,J+zJQ~$7mTDJ2>:!fn>%_";

    public static void setString(Context cntxt, String key, String value) {
        SharedPreferences.Editor editor = getEditor(cntxt);
        editor.putString(key, value);
        editor.commit();
    }

    public static void setEncryptedString(Context cntxt, String key, String value) {
        String encrypted = EncryptionManager.encrypt(value, SECRET_KEY);
        setString(cntxt, key, encrypted);
    }

    public static void setInt(Context cntxt, String key, int value) {
        SharedPreferences.Editor editor = getEditor(cntxt);
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(Context cntxt, String key) {
        return getSharedPreferences(cntxt).getString(key, null);
    }

    public static String getEncryptedString(Context cntxt, String key) {
        String encrypted = getString(cntxt, key);
        String decrypted = EncryptionManager.decrypt(encrypted, SECRET_KEY);
        return decrypted;
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
