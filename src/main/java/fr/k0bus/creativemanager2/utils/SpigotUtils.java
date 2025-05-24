package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings({"deprecation"})
public class SpigotUtils {

    public static String getPlayerDisplayname(Player player) {
        if (!isPaper()) return player.getDisplayName();
        else
            return net.kyori.adventure.text.Component.textOfChildren(player.displayName())
                    .content();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static String getPluginName() {
        if (!isPaper())
            return CreativeManager2.getAPI().getInstance().getDescription().getName();
        else return CreativeManager2.getAPI().getInstance().getPluginMeta().getName();
    }

    public static void setItemMetaLore(ItemMeta itemMeta, List<String> lore) {
        if (!isPaper()) itemMeta.setLore(lore);
        else {
            List<net.kyori.adventure.text.Component> lorePaper = new ArrayList<>();
            lore.forEach(line -> lorePaper.add(net.kyori.adventure.text.Component.text(line)));
            itemMeta.lore(lorePaper);
        }
    }

    public static void setItemMetaDisplayname(ItemMeta itemMeta, String displayName) {
        if (!isPaper()) itemMeta.setDisplayName(displayName);
        else {
            itemMeta.displayName(net.kyori.adventure.text.Component.text(displayName));
        }
    }

    public static boolean isPaper() {
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }

    public static Inventory createInventory(int row, String title) {
        if (!isPaper()) return Bukkit.createInventory(null, 9 * row, title);
        else return Bukkit.createInventory(null, 9 * row, Component.text(title));
    }

    public static boolean isBrigadier() {
        try {
            Class.forName("io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents");
            return true;
        } catch (ClassNotFoundException ignored) {
        }
        return false;
    }
}
