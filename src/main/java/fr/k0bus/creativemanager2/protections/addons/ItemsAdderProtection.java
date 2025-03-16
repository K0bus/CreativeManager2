package fr.k0bus.creativemanager2.protections.addons;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.Events.CustomEntityDeathEvent;
import dev.lone.itemsadder.api.Events.FurnitureInteractEvent;
import dev.lone.itemsadder.api.Events.FurniturePlaceEvent;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ItemsAdderProtection extends Protection {
    public ItemsAdderProtection() {
        super(Material.WRITABLE_BOOK, "ItemsAdder");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlaceFurniture(FurniturePlaceEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(CustomBlockPlaceEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockInteract(CustomBlockInteractEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnitureInteract(FurnitureInteractEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDie(CustomEntityDeathEvent event) {
        if (isDisabled()) return;
        if (!(event.getKiller() instanceof Player player)) return;
        if (hasPermission(player)) return;
        if (!Protection.isCreativePlayer(player)) return;
        player.setLastDamage(0);
        sendPermissionMessage(player);
    }
}
