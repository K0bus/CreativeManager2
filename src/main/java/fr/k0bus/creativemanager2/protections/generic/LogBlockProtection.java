package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CM2BlockData;
import fr.k0bus.creativemanager2.utils.BlockUtils;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.UUID;

public class LogBlockProtection extends Protection {
    public LogBlockProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        if(isDisabled()) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block:blocks) {
            CM2BlockData.register(block, event.getPlayer());
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if(CM2Utils.isCreativePlayer(event.getPlayer()) || hasPermission(event.getPlayer()))
        {
            List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
            for (Block block:blocks) {
                CM2BlockData.unregister(block);
            }
            return;
        }

        UUID uuid = CM2BlockData.findPlayer(event.getBlock());
        if(uuid == null) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block:blocks) {
            CM2BlockData.unregister(block);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockPhysicsEvent(BlockPhysicsEvent event){
        Block block = event.getBlock();
        if(block.getType().equals(Material.AIR)) return;
        if(block.getBlockData().isSupported(block)) return;
        UUID uuid = CM2BlockData.findPlayer(block);
        if(uuid != null)
        {
            event.setCancelled(true);
            block.setType(Material.AIR);
            CM2BlockData.unregister(block);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockGrow(BlockGrowEvent event)
    {
        //TODO: Check function don't work
        Block block = event.getBlock();
        UUID uuid;
        switch (event.getBlock().getType())
        {
            case CACTUS:
            case SUGAR_CANE:
                uuid = CM2BlockData.findPlayer(block.getRelative(BlockFace.DOWN));
                if(uuid != null)
                {
                    CM2BlockData.register(block, uuid);
                }
                break;
            case PUMPKIN:
            case MELON:
                for(Block b: BlockUtils.getAdjacentBlocks(block))
                {
                    if(Tag.CROPS.isTagged(b.getType()))
                    {
                        uuid = CM2BlockData.findPlayer(block);
                        if(uuid != null)
                        {
                            CM2BlockData.register(block, uuid);
                        }
                    }
                }
                break;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onStructureGrow(StructureGrowEvent event){
        UUID uuid = CM2BlockData.findPlayer(event.getLocation());
        if(uuid != null)
        {
            for(BlockState block: event.getBlocks())
            {
                CM2BlockData.register(block.getBlock(), uuid);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockSpread(BlockSpreadEvent event)
    {
        //TODO: Check function don't work
        UUID uuid = CM2BlockData.findPlayer(event.getSource());
        if(uuid != null)
        {
            CM2BlockData.register(event.getBlock(), uuid);
        }
    }

    @EventHandler
    public void onFallBlock(EntityChangeBlockEvent event) {

        UUID uuid = CM2BlockData.findPlayer(event.getBlock());
        if(uuid == null) return;
        if(event.getEntity() instanceof FallingBlock fallingBlock)
        {
            NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), "UUID");
            fallingBlock.getPersistentDataContainer()
                    .set(namespacedKeyUuid, PersistentDataType.STRING, uuid.toString());
            fallingBlock.setDropItem(false);
        }
        if(event.getTo().equals(Material.AIR))
        {
            CM2BlockData.unregister(event.getBlock());
        }
    }
    @EventHandler
    public void onFallBlockStop(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof FallingBlock fallingBlock)
        {
            NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), "UUID");
            String s = fallingBlock.getPersistentDataContainer()
                    .get(namespacedKeyUuid, PersistentDataType.STRING);
            if(s == null) return;
            UUID uuid = UUID.fromString(s);
            if(!event.getTo().equals(Material.AIR))
            {
                CM2BlockData.register(event.getBlock(), uuid);
            }
        }
    }

}
