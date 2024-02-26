package fr.k0bus.creativemanager2.file;

import fr.k0bus.config.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

public class Settings extends Configuration {

    JavaPlugin instance;

    public Settings(JavaPlugin instance) {
        super("config.yml", instance);
        this.instance = instance;
    }

    public String getLang()
    {
        return getString("plugin.lang");
    }
    public String getTag(){return getString("plugin.tag");}

    public boolean debugMode(){
        return getBoolean("plugin.debug-mode");
    }
}
