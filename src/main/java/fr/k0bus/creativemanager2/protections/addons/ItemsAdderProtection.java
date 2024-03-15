package fr.k0bus.creativemanager2.protections.addons;

import dev.lone.itemsadder.api.Events.*;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ItemsAdderProtection extends Protection {
    public ItemsAdderProtection(CreativeManager2 plugin) {
        super(plugin, Material.WRITABLE_BOOK);
    }

    @Override
    public boolean isCompatible() {
        return getPlugin().getServer().getPluginManager().isPluginEnabled("ItemsAdder");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlaceFurniture(FurniturePlaceEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(CustomBlockPlaceEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(CustomBlockBreakEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockInteract(CustomBlockInteractEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFurnitureInteract(FurnitureInteractEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDie(CustomEntityDeathEvent event)
    {
        if(isDisabled()) return;
        if(!(event.getKiller() instanceof Player player)) return;
        if(hasPermission(player)) return;
        if(!CM2Utils.isCreativePlayer(player)) return;
        player.setLastDamage(0);
        sendPermissionMessage(player);
    }
}
