package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class CreativeManager2 extends JavaPlugin {

    private HashMap<String, Protection> protections;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.protections = CM2Utils.loadProtections(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
