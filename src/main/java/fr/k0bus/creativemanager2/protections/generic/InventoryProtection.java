package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InventoryProtection extends Protection {
    public InventoryProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onGMChange(PlayerGameModeChangeEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;

        World world = event.getPlayer().getWorld();
        GameMode GMFrom = event.getPlayer().getGameMode();
        GameMode GMTo = event.getNewGameMode();
        String inventoryFromName = CreativeManager2.API.getInventoryName(world, GMFrom);
        String inventoryToName = CreativeManager2.API.getInventoryName(world, GMTo);
        if(inventoryFromName.equals(inventoryToName)) return;
        //TODO: Inventory save & load
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        String inventoryFromName = CreativeManager2.API
                .getInventoryName(event.getFrom().getWorld(), event.getPlayer().getGameMode());
        String inventoryToName = CreativeManager2.API
                .getInventoryName(event.getTo().getWorld(), event.getPlayer().getGameMode());
        if(inventoryToName.equals(inventoryFromName)) return;
        //TODO: Inventory save & load
    }

}
