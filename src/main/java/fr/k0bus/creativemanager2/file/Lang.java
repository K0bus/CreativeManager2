package fr.k0bus.creativemanager2.file;

import fr.k0bus.creativemanager2.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Lang extends Configuration{

    private final String lang;

    public Lang(String lang, JavaPlugin instance) {
        super(lang + ".yml", instance, "lang");
        this.lang = lang;
    }

    public void init()
    {
        loadConfig();
        FileConfiguration defaultConfig = null;
        InputStream is = plugin.getResource("lang/" + this.lang + ".yml");
        if(is != null)
            defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        else
        {
            is = plugin.getResource("lang/en_US.yml");
            if(is != null)
                defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        }

        if(defaultConfig != null)
            for(String key: defaultConfig.getKeys(false))
            {
                if(defaultConfig.isConfigurationSection(key))
                    this.checkSection(defaultConfig.getConfigurationSection(key));
                if(!configuration.contains(defaultConfig.getCurrentPath() + "." +key))
                    configuration.set(defaultConfig.getCurrentPath() + "." + key, defaultConfig.get(key));
            }
        super.save();
    }

    protected void checkSection(ConfigurationSection section)
    {
        if(section == null) return;
        for(String key: section.getKeys(false))
        {
            if(section.isConfigurationSection(key))
                checkSection(section.getConfigurationSection(key));
            if(!configuration.contains(section.getCurrentPath() + "." +key))
                configuration.set(section.getCurrentPath() + "." + key, section.get(key));
        }
    }

    public String getString(String path)
    {
        return StringUtils.translateColor(configuration.getString(path));
    }
}
