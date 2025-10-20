package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.SpigotUtils;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class PickItemProtection extends Protection {
    public PickItemProtection() {
        super(Material.ITEM_FRAME);
    }

    @Override
    public boolean isCompatible() {
        return SpigotUtils.isPaper();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPickItem(PlayerPickItemEvent event) {
        if (isDisabled()) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        if (hasPermission(event.getPlayer())) return;
        event.setCancelled(true);
    }
}
