package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.file.*;
import fr.k0bus.creativemanager2.gui.MenuListener;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class CM2API {
    private final Settings settings;
    private final Lang lang;
    private HashMap<String, Protection> protections;
    private final CreativeManager2 instance;
    private final MenuListener menuListener;
    public String TAG;
    private final HashMap<String, Set<Material>> tagMap = new HashMap<>();
    private boolean paper = false;

    public CM2API(CreativeManager2 instance)
    {
        this.instance = instance;

        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            this.paper = true;
        } catch (ClassNotFoundException ignored) {}

        this.menuListener = new MenuListener();
        this.settings = new Settings(instance);
        this.lang = new Lang(settings.getLang(), instance);
        this.TAG = StringUtils.parse(settings.getTag());
        instance.getServer().getPluginManager().registerEvents(menuListener, instance);
        loadTags();
    }

    public void loadProtections()
    {
        this.protections = CM2Utils.loadProtections(instance);
    }

    public CreativeManager2 getInstance() {
        return instance;
    }

    public HashMap<String, Protection> getProtections() {
        return protections;
    }

    public Lang getLang() {
        return lang;
    }

    public Settings getSettings() {
        return settings;
    }

    public void reloadSettings()
    {
        settings.loadConfig();
        lang.loadConfig();
        this.TAG = StringUtils.parse(settings.getTag());
    }

    public String getInventoryName(World world, GameMode gameMode) {
        if(this.settings.isString("multi-inventories." + world.getName() + "." + gameMode.name().toLowerCase()))
        {
            return this.settings
                    .getString("multi-inventories." + world.getName() + "." + gameMode.name().toLowerCase());
        }
        return this.settings
                .getString("multi-inventories._GLOBAL." + gameMode.name().toLowerCase());
    }

    private void loadTags()
    {
        try
        {
            Field[] fieldlist = Tag.class.getDeclaredFields();
            for (Field fld : fieldlist) {
                try {
                    Set<Material> set = ((Tag<Material>) fld.get(null)).getValues();
                    tagMap.put(fld.getName(), set);
                }catch (Exception ignored)
                {}
            }
            instance.getLogger().log(Level.INFO, "&2Tag loaded from Spigot ! &7[" + tagMap.size() + "]");
        }
        catch (Exception e)
        {
            instance.getLogger().log(Level.WARNING, "&cThis minecraft version could not use the TAG system.");
        }
    }

    public boolean isPaper() {
        return paper;
    }

    public HashMap<String, Set<Material>> getTagMap() {
        return this.tagMap;
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }
}
