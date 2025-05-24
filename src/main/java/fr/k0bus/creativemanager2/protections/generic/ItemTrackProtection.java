package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.SpigotUtils;
import fr.k0bus.creativemanager2.utils.TextUtils;
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
    public ItemTrackProtection() {
        super(Material.NAME_TAG);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isDisabled()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (hasPermission(player)) return;
            if (!Protection.isCreativePlayer(player)) return;
            asyncCheck(player, event);
        }
    }

    private void asyncCheck(Player player, InventoryCreativeEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getSlot() > player.getInventory().getSize()) return;
                addLore(player.getInventory().getItem(event.getSlot()), player);
            }
        }.runTaskLaterAsynchronously(CreativeManager2.getAPI().getInstance(), 2L);
    }

    private void addLore(ItemStack itemStack, HumanEntity p) {
        if (itemStack == null || p == null) return;
        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return;

        List<String> lore = getConfig().getStringList("lore");
        List<String> tempLore = new ArrayList<>();
        String displayname = TextUtils.parse(getConfig().getString("displayname"));

        if (displayname != null && !displayname.isEmpty()) {
            SpigotUtils.setItemMetaDisplayname(meta, getFinalString(displayname, (Player) p, itemStack));
        }
        if (!lore.isEmpty()) {
            for (String line : lore) {
                tempLore.add(getFinalString(line, (Player) p, itemStack));
            }
            SpigotUtils.setItemMetaLore(meta, tempLore);
        }
        itemStack.setItemMeta(meta);
    }

    private String getFinalString(String string, Player player, ItemStack itemStack) {
        return TextUtils.parse(TextUtils.replacePlaceholders(
                string,
                Map.of(
                        "PLAYER", player.getName(),
                        "UUID", player.getUniqueId().toString(),
                        "ITEM", CreativeManager2.getAPI().getMinecraftLang().get(itemStack))));
    }
}
