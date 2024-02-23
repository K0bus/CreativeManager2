package fr.k0bus.creativemanager2;

import fr.k0bus.config.Lang;
import fr.k0bus.creativemanager2.file.Settings;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.World;

import java.util.HashMap;

public class CM2API {
    private final Settings settings;
    private final Lang lang;
    private final HashMap<String, Protection> protections;
    private final CreativeManager2 instance;
    public String TAG;

    public CM2API(CreativeManager2 instance)
    {
        this.instance = instance;
        this.protections = CM2Utils.loadProtections(instance);
        this.settings = new Settings(instance);
        this.lang = new Lang(settings.getLang(), instance);
        this.TAG = StringUtils.parse(settings.getTag());
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
}
