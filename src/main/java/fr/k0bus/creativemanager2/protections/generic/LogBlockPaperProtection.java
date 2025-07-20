package fr.k0bus.creativemanager2.protections.generic;

import com.destroystokyo.paper.event.block.BlockDestroyEvent;
import fr.k0bus.creativemanager2.CM2Data;
import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.SpigotUtils;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;

public class LogBlockPaperProtection extends Protection {
    public LogBlockPaperProtection() {
        super(Material.FILLED_MAP);
    }

    @Override
    public boolean isCompatible() {
        return SpigotUtils.isPaper();
    }

    @EventHandler(ignoreCancelled = true)
    void onBlockDestroy(BlockDestroyEvent event) {
        if (isDisabled()) return;

        Block block = event.getBlock();

        UUID uuid = CM2Data.findPlayer(block);
        if (uuid != null) {
            event.setWillDrop(false);
            event.setExpToDrop(0);
            CM2Data.unregister(block);
            CM2Logger.debug("[onBlockDestroy] Block placed by " + uuid + " destroyed");
        }
    }
}
