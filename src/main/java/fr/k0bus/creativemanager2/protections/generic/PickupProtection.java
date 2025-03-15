package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class PickupProtection extends Protection {
    public PickupProtection() {
        super(Material.PAPER);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getEntity())) return;
        if (!Protection.isCreativePlayer(event.getEntity())) return;
        event.setCancelled(true);
    }
}
