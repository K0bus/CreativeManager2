package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class ContainerProtection extends Protection {
    public ContainerProtection(CreativeManager2 plugin) {
        super(plugin, Material.CHEST);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if (!CM2Utils.isProtectedChest(event.getInventory())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if (event.getBlock() instanceof Container container) {
            container.getInventory().clear();
            sendPermissionMessage(event.getPlayer());
        }
    }
}
