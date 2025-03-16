package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings({"deprecation"})
public class SpigotUtils {

    public static String getPlayerDisplayname(Player player) {
        if (!Utils.isPaper()) return player.getDisplayName();
        else
            return net.kyori.adventure.text.Component.textOfChildren(player.displayName())
                    .content();
    }

    public static String getPluginName() {
        if (!Utils.isPaper())
            return CreativeManager2.getAPI().getInstance().getDescription().getName();
        else return CreativeManager2.getAPI().getInstance().getPluginMeta().getName();
    }

    public static void setItemMetaLore(ItemMeta itemMeta, List<String> lore) {
        if (!Utils.isPaper()) itemMeta.setLore(lore);
        else {
            List<net.kyori.adventure.text.Component> lorePaper = new ArrayList<>();
            lore.forEach(line -> lorePaper.add(net.kyori.adventure.text.Component.text(line)));
            itemMeta.lore(lorePaper);
        }
    }

    public static void setItemMetaDisplayname(ItemMeta itemMeta, String displayName) {
        if (!Utils.isPaper()) itemMeta.setDisplayName(displayName);
        else {
            itemMeta.displayName(net.kyori.adventure.text.Component.text(displayName));
        }
    }
}
