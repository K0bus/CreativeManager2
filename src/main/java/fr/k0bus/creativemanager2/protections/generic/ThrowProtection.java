package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ThrowProtection extends Protection {
    public ThrowProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(ProjectileLaunchEvent event)
    {
        if(isDisabled()) return;
        if(!(event.getEntity().getShooter() instanceof Player player)) return;
        if(hasPermission(player)) return;
        if(!CM2Utils.isCreativePlayer(player)) return;
        event.setCancelled(true);
        sendPermissionMessage(player);
    }
}
