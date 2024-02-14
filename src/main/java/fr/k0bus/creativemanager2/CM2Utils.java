package fr.k0bus.creativemanager2;

import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.protections.generic.*;
import org.bukkit.GameMode;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CM2Utils {
    public static boolean isProtectedChest(Inventory inventory) {
        if (inventory.getType().equals(InventoryType.ENDER_CHEST)) return true;
        if (getProtectedType().contains(inventory.getType())) {
            if (inventory.getHolder() != null)
                return inventory.getHolder().getClass().toString().contains("org.bukkit");
        }
        return false;
    }

    public static List<InventoryType> getProtectedType() {
        List<InventoryType> typeList = new ArrayList<>();
        try {typeList.add(InventoryType.CHEST);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.FURNACE);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.BLAST_FURNACE);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.SMOKER);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.BARREL);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.BEACON);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.BREWING);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.DISPENSER);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.DROPPER);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.HOPPER);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.SHULKER_BOX);}catch (NoSuchFieldError ignored){}
        try {typeList.add(InventoryType.LECTERN);}catch (NoSuchFieldError ignored){}
        return typeList;
    }

    public static boolean isCreativePlayer(LivingEntity entity)
    {
        if(entity instanceof Player p)
        {
            return p.getGameMode().equals(GameMode.CREATIVE);
        }
        return false;
    }

    public static HashMap<String, Protection> loadProtections(CreativeManager2 plugin) {
        HashMap<String, Protection> protectionHashMap = new HashMap<>();
        Reflections reflections = new Reflections("fr.k0bus.creativemanager2.protections");
        Set<Class<? extends Protection>> classes = reflections.getSubTypesOf(Protection.class);
        for (Class<? extends Protection> aClass : classes) {
            try {
                Protection protection = (Protection) aClass.getConstructors()[0].newInstance(plugin);
                protectionHashMap.put(protection.getId(), protection);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return protectionHashMap;
    }
}
