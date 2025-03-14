package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.commands.CM2BrigadierCommand;
import fr.k0bus.creativemanager2.commands.CM2BukkitCommands;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreativeManager2 extends JavaPlugin {

    public static CM2API api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        api = new CM2API(this);
        registerCommands();
        api.initialize();
    }

    public void registerCommands() {
        if (CM2Utils.isPaper()) new CM2BrigadierCommand(this).build();
        else new CM2BukkitCommands().register(this);
    }

    @Override
    public void onDisable() {
        if (api != null) api = null;
    }
}
