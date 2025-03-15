package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.util.*;
import org.bukkit.Material;

public class ListUtils {

    private static final String SEMILICON = "*";

    public static boolean inList(String search, List<String> list) {
        List<String> lowerList = StringUtils.listToLowerCase(list);
        for (String s : lowerList) {
            if (SEMILICON.equals(s)) return true;
            if (s.isEmpty()) continue;
            if (s.startsWith(SEMILICON) && s.endsWith(SEMILICON) && search.contains(s.substring(1, s.length() - 1)))
                return true;
            if (s.startsWith(SEMILICON) && search.endsWith(s.substring(1))) return true;
            if (s.endsWith(SEMILICON) && search.startsWith(s.substring(0, s.length() - 1))) return true;
            if (s.equals(search)) return true;
            if (s.startsWith("#")) {
                Set<Material> set =
                        CreativeManager2.getAPI().getTagMap().get(s.substring(1).toUpperCase(Locale.getDefault()));
                if (set != null) {
                    if (set.contains(Material.valueOf(search.toUpperCase(Locale.getDefault())))) return true;
                } else {
                    CM2Logger.warn("Unable to find {0} tags", s);
                }
            }
        }
        return false;
    }
}
