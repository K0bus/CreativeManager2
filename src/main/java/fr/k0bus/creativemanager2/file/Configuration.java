package fr.k0bus.creativemanager2.file;

import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class Configuration {

    JavaPlugin plugin;
    File file;
    FileConfiguration configuration;
    String filename;

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
            dir.mkdirs();
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
                this.file.getParentFile().mkdirs();
                try {
                    this.file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(file.exists())
        {
            try {
                this.configuration = loadConfiguration(this.file);
            } catch (InvalidConfigurationException e)
            {
                plugin.getLogger().log(Level.SEVERE, filename + " can't be loaded ! Check file syntax first !");
                plugin.getLogger().log(Level.SEVERE, e.getMessage());
                File renamed = new File(file.getParentFile(), filename + ".old");
                if(renamed.exists())
                    renamed.delete();
                file.renameTo(renamed);
                loadConfig();
            }
            catch (IOException ex)
            {
                plugin.getLogger().log(Level.SEVERE, "Can't read file " + filename);
                this.configuration = new YamlConfiguration();
            }
        }
        else
        {
            this.configuration = new YamlConfiguration();
        }
    }

    private static YamlConfiguration loadConfiguration(File file) throws IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        return config;
    }
    public void save()
    {
        try {
            this.configuration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
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
    public Set<String> getKeysFromPath(String path, boolean deep){return this.configuration.getConfigurationSection(path).getKeys(deep); }
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
            e.printStackTrace();
        }
    }
    public static void updateConfig(String cfg, JavaPlugin plugin)
    {
        File file = new File(plugin.getDataFolder(), cfg);
        file.getParentFile().mkdirs();
        if(!file.exists())
            plugin.saveResource(cfg, false);
        FileConfiguration default_conf = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(cfg)));
        FileConfiguration conf = null;
        try {
            conf = loadConfiguration(file);
        } catch (InvalidConfigurationException e)
        {
            plugin.getLogger().log(Level.SEVERE, cfg + " can't be loaded ! Check file syntax first !");
            plugin.getLogger().log(Level.SEVERE, e.getMessage());
            File renamed = new File(file.getParentFile(), cfg + ".old");
            if(renamed.exists())
                renamed.delete();
            file.renameTo(renamed);
            return;
        }
        catch (IOException ex)
        {
            plugin.getLogger().log(Level.SEVERE, "Can't read file " + cfg);
        }
        for (String path : default_conf.getKeys(true)) {
            if(!conf.contains(path) || conf.get(path).getClass().getName() != default_conf.get(path).getClass().getName())
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
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }
}
