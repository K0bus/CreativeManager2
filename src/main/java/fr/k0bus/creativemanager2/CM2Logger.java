package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.utils.StringUtils;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CM2Logger {
    public static void info(String string, Object... args) {
        if (getLogger().isLoggable(Level.INFO)) {
            getLogger().info(formatMessage(string, args));
        }
    }

    public static void warn(String string, Object... args) {
        if (getLogger().isLoggable(Level.WARNING)) getLogger().warning(formatMessage(string, args));
    }

    public static void severe(String string, Object... args) {
        if (getLogger().isLoggable(Level.SEVERE)) getLogger().severe(formatMessage(string, args));
    }

    public void debug(String string, Object... args) {
        if (CreativeManager2.api.getSettings().debugMode() && getLogger().isLoggable(Level.INFO))
            getLogger().info("Debug >> " + StringUtils.parse(formatMessage(string, args)));
    }

    public static void exception(Exception e) {
        if (getLogger().isLoggable(Level.SEVERE)) getLogger().log(Level.SEVERE, "An error has occurred.", e);
    }

    private static Logger getLogger() {
        return CreativeManager2.api.getInstance().getLogger();
    }

    public static String formatMessage(String template, Object... args) {
        return MessageFormat.format(template, args);
    }
}
