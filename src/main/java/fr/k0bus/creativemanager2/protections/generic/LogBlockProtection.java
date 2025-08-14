package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CM2Data;
import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.BlockUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;

public class LogBlockProtection extends Protection {
    public LogBlockProtection() {
        super(Material.MAP);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (isDisabled()) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block : blocks) {
            CM2Logger.debug("[onPlace] Block placed by " + event.getPlayer().getUniqueId());
            CM2Data.register(block, event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMultiPlace(BlockMultiPlaceEvent event) {
        if (isDisabled()) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        for (BlockState state : event.getReplacedBlockStates()) {
            CM2Logger.debug("[onMultiPlace] Block placed by " + event.getPlayer().getUniqueId());
            CM2Data.register(state.getBlock(), event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        if (Protection.isCreativePlayer(event.getPlayer()) || hasPermission(event.getPlayer())) {
            List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
            for (Block block : blocks) {
                CM2Logger.debug("[onBreak] Block broken by " + event.getPlayer().getUniqueId());
                CM2Data.unregister(block);
            }
            return;
        }

        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid == null) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        List<Block> blocks = BlockUtils.getBlockStructure(event.getBlock());
        for (Block block : blocks) {
            CM2Logger.debug("[onBreak 2]Block broken by " + uuid);
            CM2Data.unregister(block);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    void onBlockGrow(BlockGrowEvent event) {
        Block block = event.getBlock();
        UUID uuid;
        Material material = event.getNewState().getBlockData().getMaterial();
        if (material.equals(Material.CACTUS) || material.equals(Material.SUGAR_CANE)) {
            uuid = CM2Data.findPlayer(block.getRelative(BlockFace.DOWN));
            if (uuid != null) {
                CM2Logger.debug("[onBlockGrow] Block grown by " + uuid);
                CM2Data.register(block, uuid);
            }
        } else if (material.equals(Material.PUMPKIN) || material.equals(Material.MELON)) {
            for (Block b : BlockUtils.getAdjacentBlocks(block)) {
                if (b.getType().equals(Material.PUMPKIN_STEM) || b.getType().equals(Material.MELON_STEM)) {
                    uuid = CM2Data.findPlayer(b);
                    if (uuid != null) {
                        CM2Logger.debug("[onBlockGrow 2] Block grown by " + uuid);
                        CM2Data.register(block, uuid);
                        break;
                    }
                }
            }
        } else if (material.equals(Material.CHORUS_FLOWER) || material.equals(Material.CHORUS_PLANT)) {
            for (Block b : BlockUtils.getAdjacentBlocksComplete(block)) {
                if (b.getType().equals(Material.CHORUS_FLOWER) || b.getType().equals(Material.CHORUS_PLANT)) {
                    uuid = CM2Data.findPlayer(b);
                    if (uuid != null) {
                        CM2Logger.debug("[onBlockGrow 3] Block grown by " + uuid);
                        CM2Data.register(block, uuid);
                        break;
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onStructureGrow(StructureGrowEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getLocation());
        if (uuid != null) {
            for (BlockState block : event.getBlocks()) {
                CM2Logger.debug("[onStructureGrow] Block grown by " + uuid);
                CM2Data.register(block.getBlock(), uuid);
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    void onBlockSpread(BlockSpreadEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getSource());
        if (uuid != null) {
            CM2Logger.debug("[onBlockSpread] Block spread by " + uuid);
            CM2Data.register(event.getBlock(), uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallBlock(EntityChangeBlockEvent event) {

        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid == null) return;
        if (event.getEntity() instanceof FallingBlock fallingBlock) {
            CM2Logger.debug("[onFallBlock] Block falling by " + uuid);
            CM2Data.register(fallingBlock, uuid);
            fallingBlock.setDropItem(false);
        }
        if (event.getTo().equals(Material.AIR)) {
            CM2Data.unregister(event.getBlock());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeaveDecay(LeavesDecayEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid == null) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        CM2Logger.debug("[onLeaveDecay] Block decayed by " + uuid);
        CM2Data.unregister(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFallBlockStop(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock fallingBlock) {
            UUID uuid = CM2Data.findPlayer(fallingBlock);
            if (uuid == null) return;
            if (!event.getTo().equals(Material.AIR)) {
                CM2Data.register(event.getBlock(), uuid);
                CM2Logger.debug("[onFallBlockStop] Block fall stopped by " + uuid);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityBreak(EntityChangeBlockEvent event) {
        if (event.getTo().equals(Material.AIR)) {
            UUID uuid = CM2Data.findPlayer(event.getBlock());
            if (uuid != null) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                CM2Logger.debug("[onEntityBreak] Block broken by " + uuid);
                CM2Data.unregister(event.getBlock());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFormTo(BlockFromToEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid != null) {
            event.getToBlock().setType(Material.AIR);
            CM2Data.unregister(event.getToBlock());
            CM2Logger.debug("[onBlockFromTo] Block destroyed by " + uuid);
            event.setCancelled(true);
            CM2Logger.debug("[onBlockFromTo] Block flow cancelled for block placed by " + uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        event.blockList().removeIf(block -> {
            UUID uuid = CM2Data.findPlayer(block);
            if (uuid != null) {
                block.setType(Material.AIR);
                CM2Data.unregister(block);
                CM2Logger.debug("[onBlockExplode] Block destroyed by " + uuid);
                return true;
            }
            return false;
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        event.blockList().removeIf(block -> {
            UUID uuid = CM2Data.findPlayer(block);
            if (uuid != null) {
                block.setType(Material.AIR);
                CM2Data.unregister(block);
                CM2Logger.debug("[onEntityExplode] Block destroyed by " + uuid);
                return true;
            }
            return false;
        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid != null) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            CM2Data.unregister(event.getBlock());
            CM2Logger.debug("[onBlockFade] Block destroyed by " + uuid);
        }
    }

    // 🔥 Bloc brûlé
    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid != null) {
            event.setCancelled(true);
            event.getBlock().setType(Material.AIR);
            CM2Data.unregister(event.getBlock());
            CM2Logger.debug("[onBlockBurn] Block destroyed by " + uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event) {
        UUID uuid = CM2Data.findPlayer(event.getBlock());
        if (uuid != null) {
            event.setCancelled(true);
            CM2Logger.debug("[onBlockIgnite] Block ignited by " + uuid);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onExtend(BlockPistonExtendEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());
        this.pistonCheck(pistonDirection, blocks);

        Block pistonHead = event.getBlock().getRelative(event.getDirection());
        UUID uuid = CM2Data.findPlayer(pistonHead);
        if (uuid != null && pistonHead.getType().equals(Material.PISTON_HEAD)) CM2Data.register(pistonHead, uuid);
    }

    @EventHandler(ignoreCancelled = true)
    public void onRetract(BlockPistonRetractEvent event) {
        BlockFace pistonDirection = event.getDirection();
        List<Block> blocks = new ArrayList<>(event.getBlocks());

        Block pistonHead = event.getBlock().getRelative(event.getDirection().getOppositeFace());
        UUID uuid = CM2Data.findPlayer(pistonHead);
        if (uuid != null && pistonHead.getType().equals(Material.PISTON_HEAD)) CM2Data.unregister(pistonHead);

        this.pistonCheck(pistonDirection, blocks);
    }

    private void pistonCheck(BlockFace blockFace, List<Block> blocks) {
        Collections.reverse(blocks);
        for (Block toMoveBlock : blocks) {
            UUID uuid = CM2Data.findPlayer(toMoveBlock);
            if (uuid != null) {
                if (toMoveBlock.getPistonMoveReaction().equals(PistonMoveReaction.BREAK)) {
                    toMoveBlock.setType(Material.AIR);
                    CM2Data.unregister(toMoveBlock);
                } else {
                    Location movedBlock = toMoveBlock.getRelative(blockFace).getLocation();
                    CM2Data.register(movedBlock, uuid);
                    CM2Data.unregister(toMoveBlock);
                }
            }
        }
    }
}
