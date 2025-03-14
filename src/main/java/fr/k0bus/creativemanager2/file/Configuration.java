package fr.k0bus.creativemanager2.file;

import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class Configuration {

    protected final JavaPlugin plugin;
    private final File file;
    protected FileConfiguration configuration;
    private final String filename;

    public Configuration(String filename, JavaPlugin instance) {
        this.plugin = instance;
        this.filename = filename;
        this.file = new File(instance.getDataFolder(), filename);
    }

    public Configuration(String filename, JavaPlugin instance, String dirName) {
        this.plugin = instance;
        this.filename = filename;
        File dir = new File(plugin.getDataFolder(), dirName);
        if (!dir.exists() && !dir.mkdirs()) {
            CM2Logger.exception(new Exception("Can't create directory"));
            CreativeManager2.api.disableCM2();
        }
        if (dir.isDirectory()) this.file = new File(dir, filename);
        else this.file = new File(dir.getParentFile(), filename);
    }

    public void loadConfig() {
        if (!file.exists()) {
            if (plugin.getResource(filename) != null) {
                plugin.saveResource(filename, false);
            } else {
                if (!this.file.getParentFile().mkdirs()) {
                    CM2Logger.exception(new Exception("Can't create directory"));
                    CreativeManager2.api.disableCM2();
                    return;
                }
                try {
                    if (!this.file.createNewFile()) {
                        CM2Logger.exception(new Exception("Can't write config file"));
                        CreativeManager2.api.disableCM2();
                        return;
                    }
                } catch (IOException e) {
                    CM2Logger.exception(e);
                }
            }
        }
        if (file.exists()) {
            this.configuration = loadConfiguration(this.file);
        } else {
            this.configuration = new YamlConfiguration();
        }
    }

    private static YamlConfiguration loadConfiguration(File file) {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            CM2Logger.exception(e);
        }

        return config;
    }

    public void save() {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            CM2Logger.exception(e);
        }
    }

    public String getString(String path) {
        return this.configuration.getString(path);
    }

    public boolean getBoolean(String path) {
        return this.configuration.getBoolean(path);
    }

    public int getInt(String path) {
        return this.configuration.getInt(path);
    }

    public double getDouble(String path) {
        return this.configuration.getDouble(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.configuration.getConfigurationSection(path);
    }

    public boolean contains(String path) {
        return this.configuration.contains(path);
    }

    public boolean isString(String path) {
        return this.configuration.isString(path);
    }

    public Set<String> getKeys(boolean deep) {
        return this.configuration.getKeys(deep);
    }

    public Set<String> getKeysFromPath(String path, boolean deep) {
        ConfigurationSection cs = this.configuration.getConfigurationSection(path);
        if (cs != null) return cs.getKeys(deep);
        else return new HashSet<>();
    }

    public List<String> getStringList(String path) {
        return this.configuration.getStringList(path);
    }

    public List<?> getList(String path) {
        return this.configuration.getList(path);
    }

    public File getFile() {
        return file;
    }

    public void set(String path, Object o) {
        this.configuration.set(path, o);
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            CM2Logger.exception(e);
        }
    }

    public static void updateConfig(String cfg, JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), cfg);
        if (!file.getParentFile().mkdirs()) {
            CM2Logger.exception(new Exception("Can't create directory"));
            CreativeManager2.api.disableCM2();
            return;
        }
        if (!file.exists()) plugin.saveResource(cfg, false);
        InputStream is = plugin.getResource(cfg);
        if (is == null) {
            CM2Logger.exception(new Exception("Can't found default config file"));
            CreativeManager2.api.disableCM2();
            return;
        }
        FileConfiguration defaultConf =
                YamlConfiguration.loadConfiguration(new InputStreamReader(is, StandardCharsets.UTF_8));
        FileConfiguration conf = loadConfiguration(file);
        for (String path : defaultConf.getKeys(true)) {
            Object configObj = conf.get(path);
            Object dConfigObj = defaultConf.get(path);
            if (configObj == null || (dConfigObj != null && configObj.getClass().equals(dConfigObj.getClass()))) {
                CM2Logger.warn("{0} added to {1}", path, cfg);
                conf.set(path, defaultConf.get(path));
            }
        }
        for (String path : conf.getKeys(true)) {
            Object confOption = conf.get(path);
            Object confOptionDefault = defaultConf.get(path);
            if (confOption != null && confOptionDefault != null) {
                if (!confOption.getClass().equals(confOptionDefault.getClass())) {
                    CM2Logger.warn("{0} removed to {1}", path, cfg);
                    conf.set(path, null);
                }
            }
        }
        try {
            conf.save(file);
        } catch (IOException e) {
            CM2Logger.exception(e);
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
