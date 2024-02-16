package fr.k0bus.creativemanager2.protections;

import fr.k0bus.creativemanager2.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public abstract class Protection implements Listener {

    private final String id;
    private boolean enabled = true;
    private final CreativeManager2 plugin;

    public Protection(CreativeManager2 plugin)
    {
        id = this.getClass().getSimpleName().replace("Protection", "").toLowerCase();
        this.plugin = plugin;
        if(isCompatible())
        {
            plugin.getServer().getPluginManager().registerEvents(this, plugin);
            plugin.getLogger().log(Level.INFO, "Protection '" + id + "' loaded from class (" + this.getClass().getSimpleName() + ")");
        }
        else
        {
            plugin.getLogger().log(Level.INFO, "Protection '" + id + "' unloaded for incompatibility");
        }
    }

    public boolean hasPermission(LivingEntity player)
    {
        return player.hasPermission("creativemanager." + id);
    }

    public String getId() {
        return id;
    }

    public CreativeManager2 getPlugin() {
        return plugin;
    }

    public boolean isDisabled() {
        return !enabled || !isCompatible();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCompatible() {
        return true;
    }

    public void sendPermissionMessage(CommandSender toMessage)
    {
        CM2Utils.sendMessage(toMessage, "permission." + getId());
    }
}
