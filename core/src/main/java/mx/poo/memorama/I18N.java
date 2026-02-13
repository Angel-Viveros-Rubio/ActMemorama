package mx.poo.memorama;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

/**
 * Clase que maneja la internacionalización (i18n) del juego.
 * Se encarga de cargar los textos desde archivos .properties
 * y permitir que el juego funcione en español o inglés.
 * El idioma se detecta automáticamente solo la primera vez
 * que se inicia el juego, usando el idioma del sistema.
 * Después de eso, el idioma solo cambia si el usuario lo decide.
 */
public class I18N {

    /** Bundle que contiene los textos del idioma actual */
    private static I18NBundle bundle;

    /** Idioma actual del juego ("es" o "en") */
    public static String currentLang = "es";

    /** Indica si es la primera vez que se inicializa la clase
     */
    private static boolean firstInit = true;

    /**
     * Inicializa el sistema de idiomas.
     * La primera vez detecta el idioma del sistema operativo.
     */
    public static void init() {
        if (firstInit) {
            Locale systemLocale = Locale.getDefault();
            String lang = systemLocale.getLanguage();

            if (lang.startsWith("es")) {
                currentLang = "es";
            } else {
                currentLang = "en";
            }

            firstInit = false;
        }

        Locale locale = currentLang.equals("es")
            ? new Locale("es", "ES")
            : Locale.ENGLISH;

        bundle = I18NBundle.createBundle(
            Gdx.files.internal("i18n/bundle"),
            locale
        );
    }

    /**
     * Obtiene un texto traducido usando su clave.
     *
     * @param key clave definida en el archivo .properties
     * @return texto en el idioma actual
     */
    public static String get(String key) {
        return bundle.get(key);
    }

    /**
     * Obtiene un texto traducido y reemplaza valores.
     * Se usa cuando el texto tiene placeholders como {0}, {1}, etc.
     * @param key clave del texto
     * @return texto formateado en el idioma actual
     */
    public static String format(String key, Object... args) {
        return bundle.format(key, args);
    }

    /**
     * Cambia el idioma entre español e inglés.
     * Actualiza el idioma actual y vuelve a cargar
     * los textos sin volver a detectar el idioma del sistema.
     */
    public static void toggleLanguage() {
        currentLang = currentLang.equals("es") ? "en" : "es";
        init();
    }
}
