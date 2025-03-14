package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.file.Lang;
import fr.k0bus.creativemanager2.file.Settings;
import fr.k0bus.creativemanager2.gui.MenuListener;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.utils.StringUtils;
import fr.k0bus.creativemanager2.utils.language.MinecraftLang;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;

public class CM2API {
    private Settings settings;
    private Lang lang;
    private Map<String, Protection> protections;
    private final CreativeManager2 instance;
    private MenuListener menuListener;
    public String tag;
    private final Map<String, Set<Material>> tagMap = new HashMap<>();
    private boolean paper;
    private MinecraftLang minecraftLang;

    public CM2API(CreativeManager2 instance) {
        this.instance = instance;
    }

    public void initialize() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            this.paper = true;
        } catch (ClassNotFoundException ignored) {
        }

        this.menuListener = new MenuListener();
        this.settings = new Settings(instance);
        this.settings.loadConfig();
        this.lang = new Lang(settings.getLang(), instance);
        this.lang.init();
        this.minecraftLang = new MinecraftLang(instance, settings.getLang());
        this.tag = StringUtils.parse(settings.getTag());
        instance.getServer().getPluginManager().registerEvents(menuListener, instance);
        this.loadTags();
        this.loadProtections();
    }

    public void loadProtections() {
        this.protections = CM2Utils.loadProtections(instance);
    }

    public CreativeManager2 getInstance() {
        return instance;
    }

    public Map<String, Protection> getProtections() {
        return protections;
    }

    public Lang getLang() {
        return lang;
    }

    public Settings getSettings() {
        return settings;
    }

    public void reloadSettings() {
        this.settings.loadConfig();
        this.lang.loadConfig();
        this.minecraftLang = new MinecraftLang(getInstance(), getSettings().getLang());
        this.tag = StringUtils.parse(settings.getTag());
        if (!protections.isEmpty()) {
            for (Protection protection : protections.values()) {
                protection.loadSettings();
            }
        }
    }

    public String getInventoryName(World world, GameMode gameMode) {
        if (this.settings.isString(
                "multi-inventories." + world.getName() + "." + gameMode.name().toLowerCase())) {
            return this.settings.getString("multi-inventories." + world.getName() + "."
                    + gameMode.name().toLowerCase());
        }
        return this.settings.getString(
                "multi-inventories._GLOBAL." + gameMode.name().toLowerCase());
    }

    private void loadTags() {
        try {
            Field[] fieldlist = Tag.class.getDeclaredFields();
            for (Field fld : fieldlist) {
                try {
                    Set<Material> set = ((Tag<Material>) fld.get(null)).getValues();
                    tagMap.put(fld.getName(), set);
                } catch (Exception ignored) {
                }
            }
            int size = tagMap.size();
            CM2Logger.info("§2Tag loaded from Spigot ! &7[{0}]", size);
        } catch (Exception e) {
            CM2Logger.info("§cThis minecraft version could not use the TAG system.");
        }
    }

    public boolean isPaper() {
        return paper;
    }

    public Map<String, Set<Material>> getTagMap() {
        return this.tagMap;
    }

    public MenuListener getMenuListener() {
        return menuListener;
    }

    public MinecraftLang getMinecraftLang() {
        return minecraftLang;
    }

    public void disableCM2() {
        instance.getServer().getPluginManager().disablePlugin(instance);
    }
}
