package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.file.Settings;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public final class CreativeManager2 extends JavaPlugin {

    public static CM2API API;

    @Override
    public void onEnable() {
        // Plugin startup logic
        API = new CM2API(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
