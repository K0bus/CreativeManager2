package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathProtection extends Protection {
    public DeathProtection(CreativeManager2 plugin) {
        super(plugin, Material.TOTEM_OF_UNDYING);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setDroppedExp(0);
        event.getDrops().clear();
    }
}
