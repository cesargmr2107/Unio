package org.uvigo.esei.unio.core;

import java.util.HashMap;
import java.util.Map;

public class TranslationLanguages {

    private static Map<String, String> toCodeMap;

    static {
        toCodeMap = new HashMap<String, String>() {{
            put("af", "af");
            put("ga", "ga");
            put("sq", "sq");
            put("it", "it");
            put("ar", "ar");
            put("ja", "ja");
            put("az", "az");
            put("kn", "kn");
            put("eu", "eu");
            put("ko", "ko");
            put("bn", "bn");
            put("la", "la");
            put("be", "be");
            put("lv", "lv");
            put("bg", "bg");
            put("lt", "lt");
            put("ca", "ca");
            put("mk", "mk");
            put("zh-CN", "zh-CN");
            put("ms", "ms");
            put("zh-TW", "zh-TW");
            put("mt", "mt");
            put("hr", "hr");
            put("no", "no");
            put("cs", "cs");
            put("fa", "fa");
            put("da", "da");
            put("pl", "pl");
            put("nl", "nl");
            put("pt", "pt");
            put("en", "en");
            put("ro", "ro");
            put("eo", "eo");
            put("ru", "ru");
            put("et", "et");
            put("sr", "sr");
            put("tl", "tl");
            put("sk", "sk");
            put("fi", "fi");
            put("sl", "sl");
            put("fr", "fr");
            put("es", "es");
            put("gl", "gl");
            put("sw", "sw");
            put("ka", "ka");
            put("sv", "sv");
            put("de", "de");
            put("ta", "ta");
            put("el", "el");
            put("te", "te");
            put("gu", "gu");
            put("th", "th");
            put("ht", "ht");
            put("tr", "tr");
            put("iw", "iw");
            put("uk", "uk");
            put("hi", "hi");
            put("ur", "ur");
            put("hu", "hu");
            put("vi", "vi");
            put("is", "is");
            put("cy", "cy");
            put("id", "id");
            put("yi", "yi");
            put("afrikaans", "af");
            put("irish", "ga");
            put("albanian", "sq");
            put("italian", "it");
            put("arabic", "ar");
            put("japanese", "ja");
            put("azerbaijan", "az");
            put("kannada", "kn");
            put("basque", "eu");
            put("korean", "ko");
            put("bengali", "bn");
            put("latin", "la");
            put("belorussian", "be");
            put("latvian", "lv");
            put("bulgarian", "bg");
            put("lithuanian", "lt");
            put("catalan", "ca");
            put("macedonian", "mk");
            put("chinese Simplified", "zh-CN");
            put("malay", "ms");
            put("chinese Traditional"," zh-TW");
            put("maltese", "mt");
            put("croatian", "hr");
            put("norwegian", "no");
            put("czech", "cs");
            put("persian", "fa");
            put("danish", "da");
            put("polish", "pl");
            put("dutch", "nl");
            put("portuguese", "pt");
            put("english", "en");
            put("romanian", "ro");
            put("esperanto", "eo");
            put("russian", "ru");
            put("estonian", "et");
            put("serbian", "sr");
            put("filipino", "tl");
            put("slovak", "sk");
            put("finnish", "fi");
            put("slovenian", "sl");
            put("french", "fr");
            put("spanish", "es");
            put("galician", "gl");
            put("swahili", "sw");
            put("georgian", "ka");
            put("swedish", "sv");
            put("german", "de");
            put("tamil", "ta");
            put("greek", "el");
            put("telugu", "te");
            put("gujarati", "gu");
            put("thai", "th");
            put("haitian creole", "ht");
            put("turkish", "tr");
            put("hebrew", "iw");
            put("ukrainian", "uk");
            put("hindi", "hi");
            put("urdu", "ur");
            put("hungarian", "hu");
            put("vietnamese", "vi");
            put("icelandic", "is");
            put("welsh", "cy");
            put("indonesian", "id");
            put("yiddish", "yi");
            put("africaans", "af");
            put("irlandesa", "ga");
            put("albanes", "sq");
            put("italiano", "it");
            put("arabica", "ar");
            put("japones", "ja");
            put("azerbaiyano", "az");
            put("kannada", "kn");
            put("vasco", "eu");
            put("coreano", "ko");
            put("bengali", "bn");
            put("latin", "la");
            put("bielorruso", "be");
            put("leton", "lv");
            put("bulgaro", "bg");
            put("lituano", "lt");
            put("catalan", "ca");
            put("macedonio", "mk");
            put("chino simplificado", "zh-CN");
            put("malayo", "ms");
            put("chino tradicional"," zh-TW");
            put("maltes", "mt");
            put("croata", "hr");
            put("noruego", "no");
            put("checo", "cs");
            put("persa", "fa");
            put("danes", "da");
            put("polaco", "pl");
            put("holandes", "nl");
            put("portugues", "pt");
            put("ingles", "en");
            put("rumano", "ro");
            put("esperanto", "eo");
            put("ruso", "ru");
            put("estonio", "et");
            put("serbio", "sr");
            put("filipino", "tl");
            put("eslovaco", "sk");
            put("finlandes", "fi");
            put("esloveno", "sl");
            put("frances", "fr");
            put("espanol", "es");
            put("gallego", "gl");
            put("swahili", "sw");
            put("georgiano", "ka");
            put("sueco", "sv");
            put("aleman", "de");
            put("tamil", "ta");
            put("griego", "el");
            put("telugu", "te");
            put("gujarati", "gu");
            put("tailandes", "th");
            put("criollo haitiano", "ht");
            put("turco", "tr");
            put("hebreo", "iw");
            put("ucranio", "uk");
            put("hindi", "hi");
            put("urdu", "ur");
            put("hungaro", "hu");
            put("vietnamita", "vi");
            put("islandes", "is");
            put("gales", "cy");
            put("indonesio", "id");
            put("yidish", "yi");

            put("chinese", "zh-CN");
            put("chino", "zh-CN");
        }};
    }

    public static String getLanguageCode(String language) {
        return toCodeMap.get(language);
    }
}
