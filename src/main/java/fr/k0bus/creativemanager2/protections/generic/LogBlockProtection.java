package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Data;
import fr.k0bus.creativemanager2.utils.BlockUtils;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.block.data.Attachable;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class LogBlockProtection extends Protection {
    public LogBlockProtection(CreativeManager2 plugin) {
        super(plugin, Material.MAP);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event)
    {
        if(isDisabled()) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block:blocks) {
            CM2Data.register(block, event.getPlayer());
        }
    }

    @EventHandler
    public void onMultiPlace(BlockMultiPlaceEvent event)
    {
        if(isDisabled()) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        for(BlockState state: event.getReplacedBlockStates())
        {
            CM2Data.register(state.getBlock(), event.getPlayer());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event)
    {
        if(CM2Utils.isCreativePlayer(event.getPlayer()) || hasPermission(event.getPlayer()))
        {
            List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
            for (Block block:blocks) {
                CM2Data.unregister(block);
            }
            return;
        }

        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if(uuid == null) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block:blocks) {
            CM2Data.unregister(block);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockPhysicsEvent(BlockPhysicsEvent event){
        if(CM2Data.findPlayer(event.getBlock()) == null) return;

        CreativeManager2.API.debug(event.getBlock().getType().name() + " > " + event.getSourceBlock().getType().name());
        CreativeManager2.API.debug("onBlockPhysicsEvent : isSolid = " + event.getSourceBlock().getType().isSolid());
        CreativeManager2.API.debug("onBlockPhysicsEvent : isSupported = " + event.getBlock().getBlockData().isSupported(event.getSourceBlock()));
        CreativeManager2.API.debug("onBlockPhysicsEvent : instanceof Attachable = " + (event.getBlock().getBlockData() instanceof Attachable));

        /*if(!event.getSourceBlock().getType().isSolid())
        {
            CreativeManager2.API.debug(
                    event.getBlock().getType().name() + ">>" + event.getSourceBlock().getType().name()
            );
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            CM2BlockData.unregister(event.getBlock());
        }*/
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
                uuid = CM2Data.findPlayer(block.getRelative(BlockFace.DOWN));
                if(uuid != null)
                {
                    CM2Data.register(block, uuid);
                }
                break;
            case PUMPKIN:
            case MELON:
                for(Block b: BlockUtils.getAdjacentBlocks(block))
                {
                    if(Tag.CROPS.isTagged(b.getType()))
                    {
                        uuid = CM2Data.findPlayer(block);
                        if(uuid != null)
                        {
                            CM2Data.register(block, uuid);
                        }
                    }
                }
                break;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onStructureGrow(StructureGrowEvent event){
        UUID uuid = CM2Data.findPlayer(event.getLocation());
        if(uuid != null)
        {
            for(BlockState block: event.getBlocks())
            {
                CM2Data.register(block.getBlock(), uuid);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockSpread(BlockSpreadEvent event)
    {
        //TODO: Check function don't work
        UUID uuid = CM2Data.findPlayer(event.getSource());
        if(uuid != null)
        {
            CM2Data.register(event.getBlock(), uuid);
        }
    }

    @EventHandler
    public void onFallBlock(EntityChangeBlockEvent event) {

        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if(uuid == null) return;
        if(event.getEntity() instanceof FallingBlock fallingBlock)
        {
            CM2Data.register(fallingBlock, uuid);
            fallingBlock.setDropItem(false);
        }
        if(event.getTo().equals(Material.AIR))
        {
            CM2Data.unregister(event.getBlock());
        }
    }
    @EventHandler
    public void onFallBlockStop(EntityChangeBlockEvent event) {
        if(event.getEntity() instanceof FallingBlock fallingBlock)
        {
            UUID uuid = CM2Data.findPlayer(fallingBlock);
            if(uuid == null) return;
            if(!event.getTo().equals(Material.AIR))
            {
                CM2Data.register(event.getBlock(), uuid);
            }
        }
    }
    @EventHandler
    public void onEntityBreak(EntityChangeBlockEvent event) {
        if (event.getTo().equals(Material.AIR)) {
            UUID uuid = CM2Data.findPlayer(event.getBlock());
            if (uuid != null) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
            }
        }
    }
    @EventHandler
    public void onExtend(BlockPistonExtendEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());
        this.pistonCheck(pistonDirection, blocks);

        Block pistonHead = event.getBlock().getRelative(event.getDirection());
        UUID uuid = CM2Data.findPlayer(pistonHead);
        if(uuid != null)
        {
            if(pistonHead.getType().equals(Material.PISTON_HEAD))
            {
                CM2Data.register(pistonHead, uuid);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onRetract(BlockPistonRetractEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());

        Block pistonHead = event.getBlock().getRelative(event.getDirection().getOppositeFace());
        UUID uuid = CM2Data.findPlayer(pistonHead);
        if(uuid != null)
        {
            if(pistonHead.getType().equals(Material.PISTON_HEAD))
            {
                CM2Data.unregister(pistonHead);
            }
        }

        this.pistonCheck(pistonDirection, blocks);
    }

    private void pistonCheck(BlockFace blockFace, List<Block> blocks) {
        Collections.reverse(blocks);
        for (Block toMoveBlock : blocks) {
            UUID uuid = CM2Data.findPlayer(toMoveBlock);
            if (uuid != null) {
                if(toMoveBlock.getPistonMoveReaction().equals(PistonMoveReaction.BREAK))
                {
                    toMoveBlock.setType(Material.AIR);
                    CM2Data.unregister(toMoveBlock);
                }
                else
                {
                    Location movedBlock = toMoveBlock.getRelative(blockFace).getLocation();
                    CM2Data.register(movedBlock, uuid);
                    CM2Data.unregister(toMoveBlock);
                }
            }
        }
    }
}
