package fr.k0bus.creativemanager2.protections.generic;

import de.tr7zw.nbtapi.NBT;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.type.ItemDataType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class DataRemoverProtection extends Protection {
    public DataRemoverProtection() {
        super(Material.NAME_TAG);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isDisabled()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (hasPermission(player)) return;
            if (!Protection.isCreativePlayer(player)) return;
            startCheck(player);
        }
    }

    private void startCheck(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is == null) continue;
                    if (ItemDataType.ENCHANT.isEnabled(DataRemoverProtection.this)) checkEnchant(is);
                    if (ItemDataType.POTION_EFFECT.isEnabled(DataRemoverProtection.this)) checkPotionEffect(is);
                    if (ItemDataType.ITEM_FLAG.isEnabled(DataRemoverProtection.this)) checkItemFlag(is);
                    if (ItemDataType.NBT.isEnabled(DataRemoverProtection.this)) checkNBT(is);
                    if (ItemDataType.NAME_SPACED_KEY.isEnabled(DataRemoverProtection.this)) checkNMS(is);
                }
            }
        }.runTaskTimer(getPlugin(), 0L, 0L);
    }

    private void checkEnchant(ItemStack itemStack) {
        for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
            itemStack.removeEnchantment(enchantment);
        }
    }

    private void checkItemFlag(ItemStack itemStack) {
        for (ItemFlag itemFlag : itemStack.getItemFlags()) {
            itemStack.removeItemFlags(itemFlag);
        }
    }

    private void checkNBT(ItemStack itemStack) {
        NBT.modify(itemStack, nbt -> {
            nbt.getKeys().forEach(nbt::removeKey);
        });
    }

    private void checkPotionEffect(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof PotionMeta potionMeta) {
            for (PotionEffect potionEffect : potionMeta.getCustomEffects()) {
                potionMeta.removeCustomEffect(potionEffect.getType());
            }
            itemStack.setItemMeta(potionMeta);
        }
    }

    private void checkNMS(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().getKeys().forEach(nms -> {
            itemMeta.getPersistentDataContainer().remove(nms);
        });
        itemStack.setItemMeta(itemMeta);
    }
}
