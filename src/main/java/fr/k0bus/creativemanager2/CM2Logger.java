package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.utils.SpigotUtils;
import fr.k0bus.creativemanager2.utils.StringUtils;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class CM2Logger {
    public static void info(String string, Object... args) {
        if (getLogger().isLoggable(Level.INFO))
            Bukkit.getConsoleSender().sendMessage(StringUtils.translateColor(getTag() + formatMessage(string, args)));
    }

    public static void warn(String string, Object... args) {
        if (getLogger().isLoggable(Level.WARNING)) getLogger().warning(formatMessage(string, args));
    }

    public static void debug(String string, Object... args) {
        if (CreativeManager2.getAPI().getSettings().debugMode() && getLogger().isLoggable(Level.INFO))
            info("Debug >> " + StringUtils.parse(formatMessage(string, args)));
    }

    public static void exception(Exception e) {
        if (getLogger().isLoggable(Level.SEVERE)) getLogger().log(Level.SEVERE, "An error has occurred.", e);
    }

    private static Logger getLogger() {
        return CreativeManager2.getAPI().getInstance().getLogger();
    }

    private static String getTag() {
        return "&7[&b" + SpigotUtils.getPluginName() + "&7]&r ";
    }

    public static String formatMessage(String template, Object... args) {
        return MessageFormat.format(template, args);
    }
}
