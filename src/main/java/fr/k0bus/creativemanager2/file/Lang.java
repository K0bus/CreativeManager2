package fr.k0bus.creativemanager2.file;

import fr.k0bus.creativemanager2.utils.StringUtils;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Lang extends Configuration {

    private final String langString;

    public Lang(String langString, JavaPlugin instance) {
        super(langString + ".yml", instance, "lang");
        this.langString = langString;
    }

    public void init() {
        loadConfig();
        FileConfiguration defaultConfig = openDefaultConfigurationFile();

        if (defaultConfig != null)
            for (String key : defaultConfig.getKeys(false)) {
                if (defaultConfig.isConfigurationSection(key))
                    this.checkSection(defaultConfig.getConfigurationSection(key));
                if (!fileConfiguration.contains(defaultConfig.getCurrentPath() + "." + key))
                    fileConfiguration.set(defaultConfig.getCurrentPath() + "." + key, defaultConfig.get(key));
            }
        super.save();
    }

    protected FileConfiguration openDefaultConfigurationFile() {
        InputStream is = plugin.getResource("lang/" + this.langString + ".yml");
        if (is != null) return YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        else {
            is = plugin.getResource("lang/en_US.yml");
            if (is != null) return YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        }
        return null;
    }

    protected void checkSection(ConfigurationSection section) {
        if (section == null) return;
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) checkSection(section.getConfigurationSection(key));
            if (!fileConfiguration.contains(section.getCurrentPath() + "." + key))
                fileConfiguration.set(section.getCurrentPath() + "." + key, section.get(key));
        }
    }

    @Override
    public String getString(String path) {
        return StringUtils.translateColor(fileConfiguration.getString(path));
    }
}
