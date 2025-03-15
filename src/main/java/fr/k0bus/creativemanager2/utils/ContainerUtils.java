package fr.k0bus.creativemanager2.utils;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ContainerUtils {
    public static boolean isProtectedChest(Inventory inventory) {
        if (inventory.getType().equals(InventoryType.ENDER_CHEST)) return true;
        if (getProtectedType().contains(inventory.getType()) && inventory.getHolder() != null) {
            return inventory.getHolder().getClass().toString().contains("org.bukkit");
        }
        return false;
    }

    public static List<InventoryType> getProtectedType() {
        List<InventoryType> typeList = new ArrayList<>();
        try {
            typeList.add(InventoryType.CHEST);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.FURNACE);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.BLAST_FURNACE);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.SMOKER);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.BARREL);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.BEACON);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.BREWING);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.DISPENSER);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.DROPPER);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.HOPPER);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.SHULKER_BOX);
        } catch (NoSuchFieldError ignored) {
        }
        try {
            typeList.add(InventoryType.LECTERN);
        } catch (NoSuchFieldError ignored) {
        }
        return typeList;
    }
}
