package dev.wumie.language;

import dev.wumie.FireQQ;
import dev.wumie.utils.misc.ResourceLocation;

import java.text.MessageFormat;
import java.util.*;

public class I18n {
    private Map<String, String> locales = new HashMap<>();
    private Map<String, String> fallbackLocales = new HashMap<>();
    private final String prefix;
    private ResourceBundle bundle;
    public I18n(Language language, String defaultPrefix) {
        this(language, defaultPrefix, false);
    }

    public I18n(Language language, String defaultPrefix, boolean silent) {
        this.prefix = defaultPrefix;
        try {
            Properties properties = new Properties();
            properties.load(new ResourceLocation("lang/"+language.getLanguageCode()+".properties").getReader());
            this.locales = new HashMap(properties);
            properties = new Properties();
            properties.load(new ResourceLocation("lang/zh_cn.properties").getReader());
            this.fallbackLocales = new HashMap(properties);
            try {
                this.bundle = ResourceBundle.getBundle("lang/", language.getLocale(), new UTF8Control());
            } catch (MissingResourceException e) {
                FireQQ.LOG.warn("Error get bundle.");
                e.printStackTrace();
            }

            if (!silent)
                FireQQ.LOG.info("Using language: " + language + " @ /lang/" + language.getLanguageCode() + ".properties");
        } catch (Exception ignored) {
        }
    }

    public String t(String key, Object... args) {
        String value = locales.get(key);
        if (value == null)
            value = fallbackLocales.get(key);
        return value == null ? "null" : MessageFormat.format(value, args).replace("%prefix%", prefix);
    }

    public String get(String key, Object... args) {
        return t(key,args);
    }

    public void info(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            FireQQ.LOG.info(line);
        }
    }

    public void warning(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            FireQQ.LOG.info(line);
        }
    }

    public void severe(String key, Object... args) {
        for (String line : t(key, args).split("\n")) {
            FireQQ.LOG.info(line);
        }
    }
}
