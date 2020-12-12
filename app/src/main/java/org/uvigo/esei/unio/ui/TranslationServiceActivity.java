package org.uvigo.esei.unio.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import org.uvigo.esei.unio.R;
import org.uvigo.esei.unio.core.TranslationLanguages;
import org.uvigo.esei.unio.core.TranslationManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class TranslationServiceActivity extends ServiceActivity {

    private TranslationManager translationManager;
    private final String DEFAULT_ORIGINAL_LANG = "es";
    private final String DEFAULT_TRANSLATION_LANG = "gl";

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        translationManager = new TranslationManager(DEFAULT_ORIGINAL_LANG,
                                                    DEFAULT_TRANSLATION_LANG);

        super.sendWelcomeMessage(String.format(getString(R.string.translation_welcome),
                DEFAULT_ORIGINAL_LANG, DEFAULT_TRANSLATION_LANG,
                getString(R.string.translation_list_command)));
    }

    @Override
    protected void processUserInput() {
        String newMessage = super.getAndSendUserInput();
        if (!newMessage.equals("")) {
            final Executor EXECUTOR = Executors.newSingleThreadExecutor();
            final Handler HANDLER = new Handler(Looper.getMainLooper());

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
             *  3. List available languages:
             *       **************************************************
             *       *   list                                         *
             *       **************************************************
             *
             */

            EXECUTOR.execute(() -> {
                String[] messageLines = newMessage.split("\n");
                String[] dividedFirstLine = messageLines[0].split(":", 2);

                String from, to;

                if ((from = TranslationLanguages.getLanguageCode(normalize(dividedFirstLine[0]))) != null
                   && (to = TranslationLanguages.getLanguageCode(normalize(dividedFirstLine[1]))) != null)
                {
                    try {
                        String translation = translationManager.translate(newMessage
                                                .substring(newMessage.indexOf('\n')), from, to);

                        HANDLER.post(() -> {sendMessage(translation);});
                    }
                    catch (TranslationManager.TranslationManagerException e) {
                        HANDLER.post(() -> {sendMessage(getString(R.string.translation_fail));});
                    }
                }
                else {
                    /// Default translation:
                    try {
                        String translation = translationManager.translate(newMessage);

                        HANDLER.post(() -> {
                            sendMessage(translation);
                        });
                    } catch (TranslationManager.TranslationManagerException e) {
                        HANDLER.post(() -> {sendMessage(getString(R.string.translation_fail));});
                    }
                }
            });
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
}
