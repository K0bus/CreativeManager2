package fr.k0bus.creativemanager2.file;

import fr.k0bus.creativemanager2.CreativeManager2;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@SuppressWarnings("unused")
public class Configuration {

    protected final JavaPlugin plugin;
    private final File file;
    protected FileConfiguration configuration;
    private final String filename;

    public Configuration(String filename, JavaPlugin instance)
    {
        this.plugin = instance;
        this.filename = filename;
        this.file = new File(instance.getDataFolder(), filename);
        loadConfig();
    }

    public Configuration(String filename, JavaPlugin instance, String dirName)
    {
        this.plugin = instance;
        this.filename = filename;
        File dir = new File(plugin.getDataFolder(), dirName);
        if(!dir.exists())
            if(!dir.mkdirs())
            {
                CreativeManager2.api.logException(new Exception("Can't create directory"));
                CreativeManager2.api.disableCM2();
            }
        if(dir.isDirectory())
            this.file = new File(dir, filename);
        else
            this.file = new File(dir.getParentFile(), filename);
        loadConfig();
    }

    public void loadConfig()
    {
        if(!file.exists())
        {
            if(plugin.getResource(filename) != null)
            {
                plugin.saveResource(filename, false);
            }
            else
            {
                if(!this.file.getParentFile().mkdirs()){
                    CreativeManager2.api.logException(new Exception("Can't create directory"));
                    CreativeManager2.api.disableCM2();
                    return;
                }
                try {
                    if(!this.file.createNewFile())
                    {
                        CreativeManager2.api.logException(new Exception("Can't write config file"));
                        CreativeManager2.api.disableCM2();
                        return;
                    }
                } catch (IOException e) {
                    CreativeManager2.api.logException(e);
                }
            }
        }
        if(file.exists())
        {
            this.configuration = loadConfiguration(this.file);
        }
        else
        {
            this.configuration = new YamlConfiguration();
        }
    }

    private static YamlConfiguration loadConfiguration(File file){
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            CreativeManager2.api.logException(e);
        }

        return config;
    }
    public void save()
    {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            CreativeManager2.api.logException(e);
        }
    }
    public String getString(String path)
    {
        return this.configuration.getString(path);
    }
    public boolean getBoolean(String path)
    {
        return this.configuration.getBoolean(path);
    }
    public int getInt(String path)
    {
        return this.configuration.getInt(path);
    }
    public double getDouble(String path)
    {
        return this.configuration.getDouble(path);
    }
    public ConfigurationSection getConfigurationSection(String path){return this.configuration.getConfigurationSection(path);}
    public boolean contains(String path){ return this.configuration.contains(path); }
    public boolean isString(String path) { return this.configuration.isString(path); }
    public Set<String> getKeys(boolean deep){return this.configuration.getKeys(deep); }
    public Set<String> getKeysFromPath(String path, boolean deep){
        ConfigurationSection cs = this.configuration.getConfigurationSection(path);
        if(cs != null)
            return cs.getKeys(deep);
        else
            return null;
    }
    public List<String> getStringList(String path){return this.configuration.getStringList(path);}
    public List<?> getList(String path){return this.configuration.getList(path);}

    public File getFile() {
        return file;
    }

    public void set(String path, Object o)
    {
        this.configuration.set(path, o);
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            CreativeManager2.api.logException(e);
        }
    }
    public static void updateConfig(String cfg, JavaPlugin plugin)
    {
        File file = new File(plugin.getDataFolder(), cfg);
        if(!file.getParentFile().mkdirs())
        {
            CreativeManager2.api.logException(new Exception("Can't create directory"));
            CreativeManager2.api.disableCM2();
            return;
        }
        if(!file.exists())
            plugin.saveResource(cfg, false);
        InputStream is = plugin.getResource(cfg);
        if(is == null)
        {
            CreativeManager2.api.logException(new Exception("Can't found default config file"));
            CreativeManager2.api.disableCM2();
            return;
        }
        FileConfiguration default_conf = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
        FileConfiguration conf = loadConfiguration(file);
        for (String path : default_conf.getKeys(true)) {
            Object configObj = conf.get(path);
            Object dConfigObj = default_conf.get(path);
            if(configObj == null || (dConfigObj != null && configObj.getClass().equals(dConfigObj.getClass())))
            {
                plugin.getLogger().log(Level.WARNING, path + " added to " + cfg);
                conf.set(path, default_conf.get(path));
            }
        }
        for (String path : conf.getKeys(true)) {
            Object confOption = conf.get(path);
            Object confOptionDefault = default_conf.get(path);
            if(confOption != null && confOptionDefault != null)
            {
                if(!default_conf.contains(path) || !confOption.getClass().getName().equals(confOptionDefault.getClass().getName()))
                {
                    plugin.getLogger().log(Level.WARNING, path + " removed to " + cfg);
                    conf.set(path, null);
                }
            }

        }
        try {
            conf.save(file);
        } catch (IOException e) {
            CreativeManager2.api.logException(e);
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
