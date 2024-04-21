package fr.k0bus.creativemanager2.file;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UserData extends Configuration {
    public UserData(Player p, JavaPlugin instance) {
        super(p.getUniqueId() + ".yml", instance, "data");
    }
}
