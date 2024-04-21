package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.commands.CM2Commands;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreativeManager2 extends JavaPlugin {

    public static CM2API API;

    @Override
    public void onEnable() {
        // Plugin startup logic
        API = new CM2API(this);
        API.loadProtections();
        new CM2Commands().register(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
