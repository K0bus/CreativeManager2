package fr.k0bus.creativemanager2.file;

import org.bukkit.plugin.java.JavaPlugin;

public class Settings extends Configuration {

    public Settings(JavaPlugin instance) {
        super("config.yml", instance);
    }

    public String getLang() {
        return getString("plugin.lang");
    }

    public String getTag() {
        return getString("plugin.tag");
    }

    public boolean debugMode() {
        return getBoolean("plugin.debug-mode");
    }
}
