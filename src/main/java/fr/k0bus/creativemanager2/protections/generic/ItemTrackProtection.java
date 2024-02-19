package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.utils.StringUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemTrackProtection extends Protection {
    public ItemTrackProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryCreativeEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getWhoClicked())) return;
        if(!CM2Utils.isCreativePlayer(event.getWhoClicked())) return;
        event.setCurrentItem(addLore(event.getCurrentItem(), event.getWhoClicked()));
        event.setCursor(addLore(event.getCursor(), event.getWhoClicked()));
        sendPermissionMessage(event.getWhoClicked());
    }

    private ItemStack addLore(ItemStack item, HumanEntity p) {
        if (item == null || p == null)
            return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        List<String> lore = CreativeManager2.API.getSettings().getStringList("protections.itemtrack.lore");
        List<String> lore_t = new ArrayList<>();

        if (lore != null) {
            for (String line : lore) {
                line = line.replace("{PLAYER}", p.getName())
                            .replace("{UUID}", p.getUniqueId().toString())
                            .replace("{ITEM}", StringUtils.proper(item.getType().name()));
                    lore_t.add(StringUtils.translateColor(line));
            }
        }
        meta.setLore(lore_t);
        item.setItemMeta(meta);
        return item;
    }
}
