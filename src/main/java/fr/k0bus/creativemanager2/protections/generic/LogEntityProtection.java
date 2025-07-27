package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CM2Data;
import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPlaceEvent;

public class LogEntityProtection extends Protection {
    public LogEntityProtection() {
        super(Material.ZOMBIE_HEAD);
    }

    @EventHandler
    public void onEntityCreation(EntityPlaceEvent event) {
        if (event.getPlayer() == null) return;
        protectEntity(event.getPlayer(), event.getEntity());
        CM2Logger.debug("Registered entity " + event.getEntity().getType().name() + " for player " + event.getPlayer().getName());
    }

    @EventHandler
    public void onHangingPlace(EntityPlaceEvent event) {
        if (event.getPlayer() == null) return;
        protectEntity(event.getPlayer(), event.getEntity());
        CM2Logger.debug("Registered hanging entity " + event.getEntity().getType().name() + " for player " + event.getPlayer().getName());
    }

    @EventHandler
    public void onEntityDie(EntityDeathEvent event) {
        if (CM2Data.findPlayer(event.getEntity()) == null) return;
        CM2Data.unregister(event.getEntity());
        event.setDroppedExp(0);
        event.getDrops().clear();
        CM2Logger.debug("Unregistered entity " + event.getEntity().getType().name());
    }

    private void protectEntity(Player player, Entity entity) {
        if (isDisabled()) return;
        if (!Protection.isCreativePlayer(player)) return;
        if (hasPermission(player)) return;

        CM2Data.register(entity, player);
        CM2Logger.debug("Registered entity " + entity.getType().name() + " for player " + player.getName());
    }
}
