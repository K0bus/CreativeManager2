package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.file.UserData;
import java.io.IOException;
import org.bukkit.entity.Player;

public class CM2Inventory {
    public static void loadInventory(Player player, String inventoryName) {
        if (CreativeManager2.getAPI().getSettings().getBoolean("stop-inventory-save")) return;
        if (player.hasPermission("creativemanager.inventory.bypass")) return;
        UserData data = new UserData(player, CreativeManager2.getAPI().getInstance());
        if (data.contains(inventoryName + ".content") && data.contains(inventoryName + ".armor")) {
            try {
                player.getInventory()
                        .setContents(
                                InventoryUtils.itemStackArrayFromBase64(data.getString(inventoryName + ".content")));
                player.getInventory()
                        .setArmorContents(
                                InventoryUtils.itemStackArrayFromBase64(data.getString(inventoryName + ".armor")));
                if (CreativeManager2.getAPI().getSettings().debugMode()) {
                    String playerDisplayname = player.getDisplayName();
                    String fileName = data.getFile().getName();
                    CM2Logger.info(
                            "Load inventory '{0}' of user '{1}' in file '{2}'",
                            inventoryName, playerDisplayname, fileName);
                }
            } catch (IOException e) {
                CM2Logger.exception(e);
            }
        } else {
            player.getInventory().clear();
            if (CreativeManager2.getAPI().getSettings().debugMode()) {
                String playerName = player.getDisplayName();
                CM2Logger.info("Clear inventory of user '{0}'", playerName);
            }
        }
    }

    public static void saveInventory(Player player, String inventoryName) {
        if (CreativeManager2.getAPI().getSettings().getBoolean("stop-inventory-save")) return;
        if (player.hasPermission("creativemanager.inventory.bypass")) return;
        UserData data = new UserData(player, CreativeManager2.getAPI().getInstance());
        String[] encoded = InventoryUtils.playerInventoryToBase64(player.getInventory());
        data.set(inventoryName + ".content", encoded[0]);
        data.set(inventoryName + ".armor", encoded[1]);
        data.save();
        if (CreativeManager2.getAPI().getSettings().debugMode()) {
            String playerDisplayname = player.getDisplayName();
            String fileName = data.getFile().getName();
            CM2Logger.info(
                    "Save inventory '{0}' of user '{1}' in file '{2}'", inventoryName, playerDisplayname, fileName);
        }
    }
}
