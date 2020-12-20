package org.uvigo.esei.unio.ui.services;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.SharedPreferencesManager;
import org.uvigo.esei.unio.core.TranslationLanguages;
import org.uvigo.esei.unio.core.TranslationManager;
import org.uvigo.esei.unio.ui.SettingsActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TranslationServiceActivity extends InternetServiceActivity {

    private TranslationManager translationManager;

    private static Spinner sourceLangS;
    private static Spinner translationLangS;

    public static final String TRANSLATE_SOURCE = "translationSourceLang";
    public static final String TRANSLATE_TRANSLATION = "translationTranslationLang";

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String defaultOriginalLang = SharedPreferencesManager
                .getString(this, TRANSLATE_SOURCE);

        if (defaultOriginalLang == null) {
            defaultOriginalLang = TranslationManager.ORIGINAL_DEFAULT_SOURCE_LANG;
        }

        String defaultTranslationLang = SharedPreferencesManager
                .getString(this, TRANSLATE_TRANSLATION);

        if (defaultTranslationLang == null) {
            defaultTranslationLang = TranslationManager.ORIGINAL_DEFAULT_TRANSLATION_LANG;
        }

        translationManager = new TranslationManager(
                TranslationLanguages.getLanguageCode(defaultOriginalLang),
                TranslationLanguages.getLanguageCode(defaultTranslationLang));

        super.sendWelcomeMessage(String.format(getString(R.string.translation_welcome),
                defaultOriginalLang, defaultTranslationLang,
                getString(R.string.translation_list_command)));
    }

    @Override
    protected void performService() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {

            /* TRANSLATION TEMPLATES:
             *
             *  1. Default Translation (default languages can be changed in app configuration):
             *       **************************************************
             *       *   <text to translate>                          *
             *       **************************************************
             *
             *  2. Custom Translation:
             *       **************************************************
             *       *   Original Language : Translation Language     *
             *       *   Text in "Original Language"                  *
             *       **************************************************
             *
             */

            String[] messageLines = newMessage.split("\n");
            String[] dividedFirstLine = messageLines[0].split(":", 2);

            String from, to;

            if ((from = TranslationLanguages.getLanguageCode(normalize(dividedFirstLine[0]))) != null
                    && (to = TranslationLanguages.getLanguageCode(normalize(dividedFirstLine[1]))) != null) {
                try {
                    String translation = translationManager.translate(newMessage
                            .substring(newMessage.indexOf('\n')), from, to);

                    sendMessage(translation);
                } catch (TranslationManager.TranslationManagerException e) {
                    sendMessage(getString(R.string.translation_fail));
                }
            } else {
                /// Default translation:
                try {
                    String translation = translationManager.translate(newMessage);
                    sendMessage(translation);
                } catch (TranslationManager.TranslationManagerException e) {
                    sendMessage(getString(R.string.translation_fail));
                }
            }
        }
    }

    private String normalize(String s) {
        String toRet = s.trim().toLowerCase();

        toRet = toRet.replace('á', 'a');
        toRet = toRet.replace('é', 'e');
        toRet = toRet.replace('í', 'i');
        toRet = toRet.replace('ó', 'o');
        toRet = toRet.replace('ú', 'u');
        toRet = toRet.replace('ñ', 'n');

        return toRet;
    }

    @Override
    public void settings(Context context) {
        AlertDialog.Builder DLG = new AlertDialog.Builder(context);
        DLG.setView(R.layout.translation_settings);
        DLG.setPositiveButton("Save", (dialog, which) -> {
            SharedPreferencesManager.setString(context, TRANSLATE_SOURCE,
                    sourceLangS.getSelectedItem().toString());

            SharedPreferencesManager.setString(context, TRANSLATE_TRANSLATION,
                    translationLangS.getSelectedItem().toString());

            Toast.makeText(context, R.string.setting_saved, Toast.LENGTH_SHORT).show();
        });
        DLG.setNegativeButton("Cancel", null);
        AlertDialog alert = DLG.create();
        alert.show();
        sourceLangS = alert.findViewById(R.id.source_language_input);
        ArrayAdapter<String> arrayAdapter
                = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                TranslationLanguages.getLanguageEngList());
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceLangS.setAdapter(arrayAdapter);
        String currentSourceLang = SharedPreferencesManager.getString(context, TRANSLATE_SOURCE);
        if (currentSourceLang == null) {
            currentSourceLang = TranslationManager.ORIGINAL_DEFAULT_SOURCE_LANG;
        }
        sourceLangS.setSelection(TranslationLanguages.getLanguageEngList().indexOf(currentSourceLang.toUpperCase()));

        translationLangS = alert.findViewById(R.id.translation_language_input);
        translationLangS.setAdapter(arrayAdapter);
        String currentTranslationLang = SharedPreferencesManager.getString(context, TRANSLATE_TRANSLATION);
        if (currentTranslationLang == null) {
            currentTranslationLang = TranslationManager.ORIGINAL_DEFAULT_TRANSLATION_LANG;
        }
        translationLangS.setSelection(TranslationLanguages.getLanguageEngList().indexOf(currentTranslationLang.toUpperCase()));
    }
}
