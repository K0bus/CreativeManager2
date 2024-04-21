package fr.k0bus.creativemanager2.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;

public class Utils {
    public static String PAPIParse(String str, OfflinePlayer player)
    {
        try {
            return PlaceholderAPI.setPlaceholders(player, str);
        }
        catch (Exception ignored){}

        return str;
    }
    public static String PAPIParse(String str)
    {
        return PAPIParse(str, null);
    }
}
