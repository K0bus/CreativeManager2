package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.file.Lang;
import fr.k0bus.creativemanager2.file.Settings;
import fr.k0bus.creativemanager2.file.language.MinecraftLang;
import fr.k0bus.creativemanager2.gui.MenuListener;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.StringUtils;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
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
    private final Map<String, Set<Material>> tagMap = new HashMap<>();
    private MinecraftLang minecraftLang;

    public CM2API(CreativeManager2 instance) {
        this.instance = instance;
    }

    public void initialize() {
        this.menuListener = new MenuListener();
        try {
            this.settings = new Settings();
            CM2Logger.info("&7> &6&lConfiguration loaded");
            this.lang = new Lang(settings.getLang());
            CM2Logger.info("&7> &6&lLanguage loaded");
        } catch (IOException e) {
            CM2Logger.exception(e);
            disableCM2();
        }
        CM2Logger.info("&7> &b&lLoading Minecraft language file");
        this.minecraftLang = new MinecraftLang(instance, settings.getLang());
        instance.getServer().getPluginManager().registerEvents(menuListener, instance);
        this.loadTags();
        this.loadProtections();
    }

    public void loadProtections() {
        this.protections = Protection.loadProtections();
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
        this.settings.reload();
        this.lang.reload();
        this.minecraftLang = new MinecraftLang(getInstance(), getSettings().getLang());
        if (!protections.isEmpty()) {
            for (Protection protection : protections.values()) {
                protection.loadSettings();
            }
        }
    }

    public String getInventoryName(World world, GameMode gameMode) {
        if (this.settings.isString(
                "multi-inventories." + world.getName() + "." + gameMode.name().toLowerCase(Locale.getDefault()))) {
            return this.settings.getString("multi-inventories." + world.getName() + "."
                    + gameMode.name().toLowerCase(Locale.getDefault()));
        }
        return this.settings.getString(
                "multi-inventories._GLOBAL." + gameMode.name().toLowerCase(Locale.getDefault()));
    }

    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("DE_MIGHT_IGNORE")
    private void loadTags() {
        try {
            Field[] fieldlist = Tag.class.getDeclaredFields();
            for (Field fld : fieldlist) {
                try {
                    Set<Material> set = ((Tag<Material>) fld.get(null)).getValues();
                    tagMap.put(fld.getName(), set);
                } catch (Exception e) { // NOPMD - suppressed EmptyCatchBlock - Try needed to catch Spigot data
                }
            }
            int size = tagMap.size();
            CM2Logger.info("&7> &6&lTag loaded from Spigot ! &r&7[{0}]", size);
        } catch (Exception e) {
            CM2Logger.info("&7> &c&lThis minecraft version could not use the TAG system.");
        }
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

    public String getTag() {
        return StringUtils.parse(settings.getTag());
    }
}
