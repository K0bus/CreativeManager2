package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropProtection extends Protection {
    public DropProtection(CreativeManager2 plugin) {
        super(plugin, Material.PAPER);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDrop(PlayerDropItemEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
}
