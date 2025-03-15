package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemTrackProtection extends Protection {
    public ItemTrackProtection(CreativeManager2 plugin) {
        super(plugin, Material.NAME_TAG);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isDisabled()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (hasPermission(player)) return;
            if (!CM2Utils.isCreativePlayer(player)) return;
            reloadInventoryLore(player);
        }
    }

    private void reloadInventoryLore(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is == null) continue;
                    addLore(is, player);
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 0L);
    }

    private void addLore(ItemStack item, HumanEntity p) {
        if (item == null || p == null) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        List<String> lore = getConfig().getStringList("lore");
        List<String> tempLore = new ArrayList<>();
        String displayname = StringUtils.parse(getConfig().getString("displayname"));

        if (displayname != null && !displayname.isEmpty()) {
            meta.setDisplayName(getFinalString(displayname, (Player) p, item));
        }
        if (!lore.isEmpty()) {
            for (String line : lore) {
                tempLore.add(getFinalString(line, (Player) p, item));
            }
            meta.setLore(tempLore);
        }
        item.setItemMeta(meta);
    }

    private String getFinalString(String string, Player player, ItemStack itemStack) {
        return StringUtils.parse(StringUtils.replacePlaceholders(
                string,
                Map.of(
                        "PLAYER", player.getName(),
                        "UUID", player.getUniqueId().toString(),
                        "ITEM", CreativeManager2.getAPI().getMinecraftLang().get(itemStack))));
    }
}
