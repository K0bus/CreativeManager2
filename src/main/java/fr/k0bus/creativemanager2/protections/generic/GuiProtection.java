package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class GuiProtection extends Protection {
    public GuiProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if(CM2Utils.isProtectedChest(event.getInventory())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
}
