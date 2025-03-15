package fr.k0bus.creativemanager2.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class Utils {
    public static String placeholderApiParse(String str, OfflinePlayer player) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI"))
            return PlaceholderAPI.setPlaceholders(player, str);
        return str;
    }

    public static String placeholderApiParse(String str) {
        return placeholderApiParse(str, null);
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
