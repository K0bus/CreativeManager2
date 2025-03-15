package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.commands.CM2BrigadierCommand;
import fr.k0bus.creativemanager2.commands.CM2BukkitCommands;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreativeManager2 extends JavaPlugin {

    private static CM2API api;

    @Override
    public void onEnable() {
        // Plugin startup logic
        setApi(this);
        registerCommands();
        api.initialize();
    }

    private static void setApi(CreativeManager2 instance) {
        api = new CM2API(instance);
    }

    private void registerCommands() {
        if (CM2Utils.isPaper()) new CM2BrigadierCommand().build();
        else new CM2BukkitCommands().register(this);
    }

    public static CM2API getAPI() {
        return api;
    }

    @Override
    public void onDisable() {}
}
