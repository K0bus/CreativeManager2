package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.ContainerUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiProtection extends Protection {
    public GuiProtection() {
        super(Material.BOOK);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        if (ContainerUtils.isProtectedChest(event.getInventory())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
}
