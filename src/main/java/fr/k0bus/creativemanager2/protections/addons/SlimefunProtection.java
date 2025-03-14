package fr.k0bus.creativemanager2.protections.addons;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import io.github.thebusybiscuit.slimefun4.api.events.MultiBlockInteractEvent;
import io.github.thebusybiscuit.slimefun4.api.events.PlayerRightClickEvent;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SlimefunProtection extends Protection {
    public SlimefunProtection(CreativeManager2 plugin) {
        super(plugin, Material.SLIME_BLOCK);
    }

    @Override
    public boolean isCompatible() {
        return getPlugin().getServer().getPluginManager().isPluginEnabled("Slimefun");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMultiBlockInteract(MultiBlockInteractEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void protectBreakWithSlimefun(BlockBreakEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void protectSlimefunItemInventory(InventoryClickEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getWhoClicked())) return;
        if (!CM2Utils.isCreativePlayer(event.getWhoClicked())) return;
        if (SlimefunItem.getByItem(event.getCurrentItem()) != null) {
            event.setCancelled(true);
            event.setCurrentItem(null);
            event.getWhoClicked().setItemOnCursor(null);
            sendPermissionMessage(event.getWhoClicked());
            return;
        }
        if (SlimefunItem.getByItem(event.getCursor()) != null) {
            event.getWhoClicked().setItemOnCursor(null);
            event.setCancelled(true);
            sendPermissionMessage(event.getWhoClicked());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void protectSlimefunItemInteract(PlayerRightClickEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if (event.getSlimefunItem().isPresent() || event.getSlimefunBlock().isPresent()) {
            event.setUseBlock(Event.Result.DENY);
            event.setUseItem(Event.Result.DENY);
            event.cancel();
            sendPermissionMessage(event.getPlayer());
        }
    }
}
