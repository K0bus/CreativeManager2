package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CM2API;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.file.UserData;
import fr.k0bus.utils.InventoryUtils;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.logging.Level;

public class CM2Inventory {
    public static void loadInventory(Player player, String inventoryName)
    {
        if(CreativeManager2.API.getSettings().getBoolean("stop-inventory-save")) return;
        if(player.hasPermission("creativemanager.inventory.bypass")) return;
        UserData data = new UserData(player, CreativeManager2.API.getInstance());
        if(data.contains(inventoryName + ".content") && data.contains(inventoryName + ".armor"))
        {
            try {
                player.getInventory().setContents(
                        InventoryUtils.itemStackArrayFromBase64(data.getString(inventoryName + ".content"))
                );
                player.getInventory().setArmorContents(
                        InventoryUtils.itemStackArrayFromBase64(data.getString(inventoryName + ".armor"))
                );
                if(CreativeManager2.API.getSettings().debugMode())
                    CreativeManager2.API.getInstance().getLogger()
                            .log(Level.INFO, "Load inventory '" + inventoryName + "' of user '" + player.getDisplayName() + "' in file '" + data.getFile().getName() + "'");
            } catch (IOException e) {
                CreativeManager2.API.getInstance().getLogger()
                        .log(Level.SEVERE, e.getMessage());
            }
        } else
        {
            player.getInventory().clear();
            if(CreativeManager2.API.getSettings().debugMode())
                CreativeManager2.API.getInstance().getLogger()
                        .log(Level.INFO, "Clear inventory of user '" + player.getDisplayName());
        }

    }
    public static void saveInventory(Player player, String inventoryName)
    {
        if(CreativeManager2.API.getSettings().getBoolean("stop-inventory-save")) return;
        if(player.hasPermission("creativemanager.inventory.bypass")) return;
        UserData data = new UserData(player, CreativeManager2.API.getInstance());
        String[] encoded = InventoryUtils.playerInventoryToBase64(player.getInventory());
        data.set(inventoryName + ".content", encoded[0]);
        data.set(inventoryName + ".armor", encoded[1]);
        data.save();
        if(CreativeManager2.API.getSettings().debugMode())
            CreativeManager2.API.getInstance().getLogger()
                    .log(Level.INFO, "Save inventory '" + inventoryName + "' of user '" + player.getDisplayName() + "' in file '" + data.getFile().getName() + "'");
    }
}
