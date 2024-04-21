package fr.k0bus.creativemanager2.file;

import fr.k0bus.creativemanager2.utils.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Lang extends Configuration{

    public Lang(String lang, JavaPlugin instance) {
        super(lang + ".yml", instance, "lang");
        FileConfiguration defaultConfig = null;

        InputStream is = plugin.getResource("lang/" + lang + ".yml");
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
                    checkSection(defaultConfig.getConfigurationSection(key));
                if(!configuration.contains(defaultConfig.getCurrentPath() + "." +key))
                    configuration.set(defaultConfig.getCurrentPath() + "." + key, defaultConfig.get(key));
            }
        this.save();
    }

    public void checkSection(ConfigurationSection section)
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
    
    public String getString(String path, HashMap<String, String> replaceMap)
    {
        String s = configuration.getString(path);
        if(s == null) return "";
        for (Map.Entry<String, String> entry:replaceMap.entrySet()) {
            s = s.replace(entry.getKey(), entry.getValue());
        }
        return StringUtils.translateColor(s);
    }
}
