package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ItemsUtils {
    public static boolean inList(ItemStack itemStack, List<String> strings)
    {
        return inList(itemStack.getType(), strings);
    }
    public static boolean inList(Material material, List<String> strings)
    {
        String materialName = material.name().toLowerCase();
        for(String s : strings)
        {
            s = s.toLowerCase();
            if(s.equals("*")) return true;
            if(s.isEmpty()) continue;
            if(s.startsWith("*") && s.endsWith("*"))
                if(materialName.contains(s.substring(1, s.length() -1)))
                    return true;
            if(s.startsWith("*"))
                if(materialName.endsWith(s.substring(1)))
                    return true;
            if(s.endsWith("*"))
                if(materialName.startsWith(s.substring(0, s.length() -1)))
                    return true;
            if(s.equals(materialName))
                return true;
            if(s.startsWith("#"))
            {
                Set<Material> set = CreativeManager2.API.getTagMap().get(s.substring(1).toUpperCase());
                if(set != null)
                {
                    if(set.contains(material)) return true;
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
