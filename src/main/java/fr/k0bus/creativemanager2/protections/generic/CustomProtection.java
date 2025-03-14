package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.type.CustomType;
import fr.k0bus.creativemanager2.type.ListType;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class CustomProtection extends Protection {
    public CustomProtection(CreativeManager2 plugin) {
        super(plugin, Material.WRITTEN_BOOK);
    }

    public ListType getType(CustomType type) {
        return ListType.fromString(getConfig().getString(type.getId() + ".type"));
    }

    public List<String> getList(CustomType type) {
        return getConfig().getStringList(type.getId() + ".list");
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (needCancel(event.getPlayer(), event.getBlock().getType().name(), CustomType.PLACE)) {
            event.setCancelled(true);
            sendPermissionMessage(event.getPlayer(), CustomType.PLACE.getId());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (needCancel(event.getPlayer(), event.getBlock().getType().name(), CustomType.BREAK)) {
            event.setCancelled(true);
            sendPermissionMessage(event.getPlayer(), CustomType.BREAK.getId());
        }
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null
                && needCancel(
                        event.getPlayer(), event.getClickedBlock().getType().name(), CustomType.BLOCK_USE)) {
            event.setCancelled(true);
            sendPermissionMessage(event.getPlayer(), CustomType.BLOCK_USE.getId());
        }
        if (event.getItem() != null
                && needCancel(event.getPlayer(), event.getItem().getType().name(), CustomType.ITEM_USE)) {
            event.setCancelled(true);
            sendPermissionMessage(event.getPlayer(), CustomType.ITEM_USE.getId());
        }
    }

    @EventHandler
    public void onCreativeInventory(InventoryCreativeEvent event) {
        if (event.getCursor().getType().equals(Material.AIR)) return;
        if (needCancel(event.getWhoClicked(), event.getCursor().getType().name(), CustomType.STORE_ITEM)) {
            event.setCancelled(true);
            event.setCursor(new ItemStack(Material.AIR));
            sendPermissionMessage(event.getWhoClicked(), CustomType.STORE_ITEM.getId());
        }
        // TODO: Implement NBT
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (needCancel(event.getPlayer(), event.getMessage(), CustomType.COMMANDS)) {
            event.setCancelled(true);
            sendPermissionMessage(event.getPlayer(), CustomType.COMMANDS.getId());
        }
    }

    private boolean needCancel(LivingEntity player, String s, CustomType type) {
        if (isDisabled()) return false;
        if (!CM2Utils.isCreativePlayer(player)) return false;
        if (hasPermission(player, type.getId())) return false;
        if (s == null) return false;
        if (hasPermission(player, type.getId() + "." + s)) return false;
        return CM2Utils.inList(s.toLowerCase(), getList(type)) == getType(type).isBlacklistMode();
    }
}
