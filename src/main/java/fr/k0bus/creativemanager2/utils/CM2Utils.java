package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.utils.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

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

    public static void sendMessage(CommandSender messageTo, String text)
    {
        if(!CreativeManager2.API.getLang().getString(text).isEmpty())
            messageTo.sendMessage(parse(StringUtils.parse(CreativeManager2.API.getLang().getString(text))));
    }
    public static void sendRawMessage(CommandSender messageTo, String text)
    {
        if(!text.isEmpty())
            messageTo.sendMessage(parse(StringUtils.parse(text)));
    }

    public static String parse(String string)
    {
        return string.replace("{TAG}", CreativeManager2.API.TAG);
    }
    public static boolean inList(ItemStack itemStack, List<String> strings)
    {
        return inList(itemStack.getType(), strings);
    }
    public static boolean inList(Block block, List<String> strings)
    {
        return inList(block.getType(), strings);
    }
    public static boolean inList(Material material, List<String> strings)
    {
        return inList(material.name().toLowerCase(), strings);
    }

    public static boolean inList(String search, List<String> list) {
        for(String s : list)
        {
            s = s.toLowerCase();
            if(s.equals("*")) return true;
            if(s.isEmpty()) continue;
            if(s.startsWith("*") && s.endsWith("*"))
                if(search.contains(s.substring(1, s.length() -1)))
                    return true;
            if(s.startsWith("*"))
                if(search.endsWith(s.substring(1)))
                    return true;
            if(s.endsWith("*"))
                if(search.startsWith(s.substring(0, s.length() -1)))
                    return true;
            if(s.equals(search))
                return true;
            if(s.startsWith("#"))
            {
                Set<Material> set = CreativeManager2.API.getTagMap().get(s.substring(1).toUpperCase());
                if(set != null)
                {
                    if(set.contains(Material.valueOf(search))) return true;
                }
                else
                {
                    CreativeManager2.API.getInstance().getLogger()
                            .log(Level.WARNING, "Unable to find " + s + " tags");
                }
            }
        }
        return false;
    }
}
