package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Data;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPlaceEvent;

public class LogEntityProtection extends Protection {
    public LogEntityProtection(CreativeManager2 plugin) {
        super(plugin, Material.ZOMBIE_HEAD);
    }

    @EventHandler
    public void onEntityCreation(EntityPlaceEvent event) {
        if (event.getPlayer() == null) return;
        if (isDisabled()) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if (hasPermission(event.getPlayer())) return;

        CM2Data.register(event.getEntity(), event.getPlayer());
    }

    @EventHandler
    public void onEntityDie(EntityDeathEvent event) {
        if (CM2Data.findPlayer(event.getEntity()) == null) return;
        event.setDroppedExp(0);
        event.getDrops().clear();
    }
}
