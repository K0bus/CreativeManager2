package fr.k0bus.creativemanager2.protections.generic;

import de.tr7zw.nbtapi.NBT;
import dev.lone.itemsadder.api.CustomStack;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.type.ItemDataType;
import fr.k0bus.creativemanager2.type.ListType;
import fr.k0bus.creativemanager2.utils.ListUtils;
import io.th0rgal.oraxen.api.OraxenItems;
import java.util.List;
import java.util.Map;
import me.RockinChaos.itemjoin.api.ItemJoinAPI;
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
        super("data-remover", Material.NAME_TAG);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isDisabled()) return;
        if (event.getWhoClicked() instanceof Player player) {
            if (hasPermission(player)) return;
            if (!Protection.isCreativePlayer(player)) return;
            asyncCheck(player);
        }
    }

    private void asyncCheck(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (ItemStack is : player.getInventory().getContents()) {
                    if (is == null) continue;
                    if (isFiltered(is)) continue;
                    if (ItemDataType.ENCHANT.isEnabled(DataRemoverProtection.this)) checkEnchant(is);
                    if (ItemDataType.POTION_EFFECT.isEnabled(DataRemoverProtection.this)) checkPotionEffect(is);
                    if (ItemDataType.ITEM_FLAG.isEnabled(DataRemoverProtection.this)) checkItemFlag(is);
                    if (ItemDataType.NBT.isEnabled(DataRemoverProtection.this)) checkNBT(is);
                    if (ItemDataType.NAME_SPACED_KEY.isEnabled(DataRemoverProtection.this)) checkNMS(is);
                }
            }
        }.runTaskLaterAsynchronously(getPlugin(), 1L);
    }

    private List<String> getFilterList(String tag) {
        return getConfig().getStringList("filter.list").stream()
                .filter(s -> s.startsWith(tag))
                .map(s -> s.substring(tag.length())) // Retire "ia:" (3 caractères)
                .toList();
    }
    private ListType getFilterType() {
        return ListType.fromString(getConfig().getString("filter.type"));
    }

    private boolean isFiltered(ItemStack itemStack) {
        if (isMaterialFiltered(itemStack)) return true;
        if (isIAFiltered(itemStack)) return true;
        if (isOraxenFiltered(itemStack)) return true;
        if (isItemJoinFiltered(itemStack)) return true;
        return false;
    }

    private boolean isMaterialFiltered(ItemStack itemStack) {
        return ListUtils.inList(
                itemStack.getType().name(),
                getFilterList("material:"),
                getFilterType());
    }

    private boolean isIAFiltered(ItemStack itemStack) {
        CustomStack customStack = CustomStack.byItemStack(itemStack);
        if (customStack == null) return false;
        return ListUtils.inList(
                customStack.getId(),
                getFilterList("ia:"),
                getFilterType());
    }
    private boolean isOraxenFiltered(ItemStack itemStack) {
        String oraxenItemsID = OraxenItems.getIdByItem(itemStack);
        if (oraxenItemsID == null) return false;
        return ListUtils.inList(
                oraxenItemsID,
                getFilterList("ox:"),
                getFilterType());
    }
    private boolean isItemJoinFiltered(ItemStack itemStack) {
        ItemJoinAPI itemAPI = new ItemJoinAPI();
        String itemJoinID = itemAPI.getNode(itemStack);
        if (itemJoinID == null) return false;
        return ListUtils.inList(
                itemJoinID,
                getFilterList("ij:"),
                getFilterType());
    }

    private void checkEnchant(ItemStack itemStack) {
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry :
                itemStack.getEnchantments().entrySet()) {
            if (ListUtils.inList(
                    enchantmentIntegerEntry.getKey().getKey().getKey(),
                    ItemDataType.ENCHANT.getList(this),
                    ItemDataType.ENCHANT.getListType(this))) {
                itemStack.removeEnchantment(enchantmentIntegerEntry.getKey());
                continue;
            }
            int maxLevel = 0;
            if (!getConfig().getBoolean("type.ENCHANT.allow-unsafe"))
                maxLevel = enchantmentIntegerEntry.getKey().getMaxLevel();
            else if (getConfig()
                    .contains("type.ENCHANT.custom-max."
                            + enchantmentIntegerEntry.getKey().getKey().getKey()))
                maxLevel = getConfig()
                        .getInt("type.ENCHANT.custom-max."
                                + enchantmentIntegerEntry.getKey().getKey().getKey());

            if (enchantmentIntegerEntry.getValue() > maxLevel)
                itemStack.addUnsafeEnchantment(enchantmentIntegerEntry.getKey(), maxLevel);
        }
    }

    private void checkItemFlag(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        for (ItemFlag itemFlag : itemMeta.getItemFlags()) {
            if (ListUtils.inList(
                    itemFlag.name(), ItemDataType.ITEM_FLAG.getList(this), ItemDataType.ITEM_FLAG.getListType(this))) {
                itemMeta.removeItemFlags(itemFlag);
                itemStack.setItemMeta(itemMeta);
            }
        }
    }

    private void checkNBT(ItemStack itemStack) {
        NBT.modify(itemStack, nbt -> {
            nbt.getKeys().forEach(nbtKey -> {
                if (ListUtils.inList(nbtKey, ItemDataType.NBT.getList(this), ItemDataType.NBT.getListType(this))) {
                    nbt.removeKey(nbtKey);
                }
            });
        });
    }

    private void checkPotionEffect(ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof PotionMeta potionMeta) {
            for (PotionEffect potionEffect : potionMeta.getCustomEffects()) {
                if (ListUtils.inList(
                        potionEffect.getType().getKey().getKey(),
                        ItemDataType.POTION_EFFECT.getList(this),
                        ItemDataType.POTION_EFFECT.getListType(this))) {
                    potionMeta.removeCustomEffect(potionEffect.getType());
                }
            }
            itemStack.setItemMeta(potionMeta);
        }
    }

    private void checkNMS(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().getKeys().forEach(nms -> {
            if (ListUtils.inList(
                    nms.getKey(),
                    ItemDataType.NAME_SPACED_KEY.getList(this),
                    ItemDataType.NAME_SPACED_KEY.getListType(this))) {
                itemMeta.getPersistentDataContainer().remove(nms);
            }
        });
        itemStack.setItemMeta(itemMeta);
    }
}
