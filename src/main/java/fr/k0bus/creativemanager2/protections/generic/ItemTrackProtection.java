package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.StringUtils;
import org.bukkit.Material;
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
        super(plugin, Material.NAME_TAG);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryCreative(InventoryCreativeEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getWhoClicked())) return;
        if(!CM2Utils.isCreativePlayer(event.getWhoClicked())) return;
        event.setCurrentItem(addLore(event.getCurrentItem(), event.getWhoClicked()));
        event.setCursor(addLore(event.getCursor(), event.getWhoClicked()));
    }

    private ItemStack addLore(ItemStack item, HumanEntity p) {
        if (item == null || p == null)
            return null;
        ItemMeta meta = item.getItemMeta();
        if (meta == null)
            return item;

        List<String> lore = getConfig().getStringList("lore");
        List<String> lore_t = new ArrayList<>();
        String displayname = StringUtils.parse(getConfig().getString("displayname"));

        if(displayname != null && !displayname.isEmpty())
        {
            meta.setDisplayName(
                    displayname.replace("{ITEM}", CreativeManager2.API.getMinecraftLang().get(item))
            );
        }
        if (!lore.isEmpty()) {
            for (String line : lore) {
                line = line.replace("{PLAYER}", p.getName())
                            .replace("{UUID}", p.getUniqueId().toString())
                            .replace("{ITEM}", CreativeManager2.API.getMinecraftLang().get(item));
                    lore_t.add(StringUtils.translateColor(line));
            }
            meta.setLore(lore_t);
        }
        item.setItemMeta(meta);
        return item;
    }
}
