package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.protections.Protection;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class EnchantProtection extends Protection {
    public EnchantProtection() {
        super(Material.ENCHANTED_BOOK);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isDisabled()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (hasPermission(player)) return;
            if (!Protection.isCreativePlayer(player)) return;
            checkInventoryEnchantments(player);
        }
    }

    private void checkInventoryEnchantments(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is == null) continue;
                    checkEnchant(is);
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 0L);
    }

    private void checkEnchant(ItemStack is) {
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry :
                is.getEnchantments().entrySet()) {
            if (getConfig()
                    .getStringList("blacklist")
                    .contains(enchantmentIntegerEntry.getKey().getName())) {
                is.removeEnchantment(enchantmentIntegerEntry.getKey());
                continue;
            }
            int maxLevel = 0;
            if (!getConfig().getBoolean("allow-unsafe"))
                maxLevel = enchantmentIntegerEntry.getKey().getMaxLevel();
            else if (getConfig()
                    .contains("custom-max." + enchantmentIntegerEntry.getKey().getName()))
                maxLevel = getConfig()
                        .getInt("custom-max." + enchantmentIntegerEntry.getKey().getName());

            if (enchantmentIntegerEntry.getValue() > maxLevel)
                is.addUnsafeEnchantment(enchantmentIntegerEntry.getKey(), maxLevel);
        }
    }
}
