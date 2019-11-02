package com.bressio.rendezvous.languages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;

import static com.bressio.rendezvous.scheme.PlayerSettings.LANG;

public class Internationalization {

    public enum Language {
        ENGLISH, BRAZILIAN_PORTUGUESE
    }

    private I18NBundle bundle;

    public Internationalization() {
        setupBundle();
    }

    private void setupBundle() {
        FileHandle baseFileHandle = Gdx.files.internal("localization/Rendezvous");
        Locale locale = new Locale("", "");
        if (LANG == Language.BRAZILIAN_PORTUGUESE) {
            locale = new Locale("pt", "BR");
        }
        bundle = I18NBundle.createBundle(baseFileHandle, locale);
    }

    public I18NBundle getBundle() {
        return bundle;
    }

    public void setLanguage(Language language) {
        FileHandle baseFileHandle = Gdx.files.internal("localization/Rendezvous");
        Locale locale = new Locale("", "");
        if (language == Language.BRAZILIAN_PORTUGUESE) {
            locale = new Locale("pt", "BR");
        }
        bundle = I18NBundle.createBundle(baseFileHandle, locale);
    }
}
