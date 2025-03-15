package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.commands.CM2BrigadierCommand;
import fr.k0bus.creativemanager2.commands.CM2BukkitCommands;
import fr.k0bus.creativemanager2.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class CreativeManager2 extends JavaPlugin {

    private static CM2API api;

    @Override
    public void onEnable() {
        setApi(this);
        long timestamp = System.currentTimeMillis();
        String separator = "▬".repeat(40);
        CM2Logger.info("&8{0}", separator);
        CM2Logger.info("&bStart initialization...");
        registerCommands();
        api.initialize();
        int elapsedTime = (int) (System.currentTimeMillis() - timestamp);
        CM2Logger.info("&bInitialization ended in {0}ms", elapsedTime);
        CM2Logger.info("&8{0}", separator);
    }

    private static void setApi(CreativeManager2 instance) {
        api = new CM2API(instance);
    }

    private void registerCommands() {
        if (Utils.isPaper()) new CM2BrigadierCommand().build();
        else new CM2BukkitCommands().register(this);
    }

    public static CM2API getAPI() {
        return api;
    }

    @Override
    public void onDisable() {}
}
