package fr.k0bus.creativemanager2;

import org.bukkit.plugin.java.JavaPlugin;

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
