package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EntityInteractionProtection extends Protection {
    public EntityInteractionProtection() {
        super("entity.interact", Material.VILLAGER_SPAWN_EGG);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEntityEvent event) {
        run(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractAtEntityEvent event) {
        run(event);
    }

    public void run(PlayerInteractEntityEvent event) {
        if (isDisabled()) return;
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof LivingEntity)) return;
        if (event.getRightClicked() instanceof Player) return;
        if (hasPermission(player)) return;
        if (!Protection.isCreativePlayer(player)) return;
        event.setCancelled(true);
        sendPermissionMessage(player);
    }
}
