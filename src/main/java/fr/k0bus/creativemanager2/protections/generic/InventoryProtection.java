package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Inventory;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class InventoryProtection extends Protection {
    public InventoryProtection(CreativeManager2 plugin) {
        super(plugin, Material.PLAYER_HEAD);
    }

    @EventHandler
    public void onGMChange(PlayerGameModeChangeEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;

        World world = event.getPlayer().getWorld();
        GameMode gameModeFrom = event.getPlayer().getGameMode();
        GameMode gameModeTo = event.getNewGameMode();
        String inventoryFromName = CreativeManager2.getAPI().getInventoryName(world, gameModeFrom);
        String inventoryToName = CreativeManager2.getAPI().getInventoryName(world, gameModeTo);
        if (inventoryFromName.equals(inventoryToName)) return;
        CM2Inventory.saveInventory(event.getPlayer(), inventoryFromName);
        CM2Inventory.loadInventory(event.getPlayer(), inventoryToName);
    }

    @EventHandler
    public void onWorldChange(PlayerTeleportEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (event.getFrom().getWorld() == null) return;
        if (event.getTo().getWorld() == null) return;

        String inventoryFromName = CreativeManager2.getAPI()
                .getInventoryName(event.getFrom().getWorld(), event.getPlayer().getGameMode());
        String inventoryToName = CreativeManager2.getAPI()
                .getInventoryName(event.getTo().getWorld(), event.getPlayer().getGameMode());
        if (inventoryToName.equals(inventoryFromName)) return;
        CM2Inventory.saveInventory(event.getPlayer(), inventoryFromName);
        CM2Inventory.loadInventory(event.getPlayer(), inventoryToName);
    }
}
