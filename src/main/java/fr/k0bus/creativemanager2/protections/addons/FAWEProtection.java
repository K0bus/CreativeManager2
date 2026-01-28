package fr.k0bus.creativemanager2.protections.addons;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import fr.k0bus.creativemanager2.CM2Data;
import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;


public class FAWEProtection extends Protection {
    public FAWEProtection() {
        super(Material.WOODEN_AXE, "FastAsyncWorldEdit");
    }

    @SuppressWarnings("checkstyle:LeftCurly")
    @Subscribe
    public void onEditSessionEnd(EditSessionEvent e) {
        if (e.getStage() != EditSession.Stage.BEFORE_CHANGE) return;
        if (e.getActor() == null || !e.getActor().isPlayer()) return;

        if (!(BukkitAdapter.adapt(e.getActor()) instanceof Player p)) return;
        World w = BukkitAdapter.adapt(e.getWorld());

        try {
            Region selection = WorldEdit.getInstance().getSessionManager().get(e.getActor()).getSelection();
            if (selection == null) {
                CM2Logger.warn("[WERC] Could not get selection for {0}", p.getName());
                return;
            }
            CM2Logger.warn("[WERC] Starting batch logging for {0} in world {1}", p.getName(), w.getName());

            int totalBlocks = 0;
            AtomicInteger savedBlocks = new AtomicInteger();
            for (BlockVector3 v : selection) {
                totalBlocks++;
                Bukkit.getScheduler().runTaskAsynchronously(CreativeManager2.getAPI().getInstance(), () -> {
                    Block b = w.getBlockAt(v.x(), v.y(), v.z());
                    if (b.getType() != Material.AIR) {
                        CM2Data.register(b, p.getUniqueId());
                        savedBlocks.getAndIncrement();
                    } else {
                        CM2Data.unregister(b);
                    }
                });

                // Avoid overloading the main thread TaskQueue
                if (totalBlocks % 1000 == 0) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
            CM2Logger.warn("[WERC] Logging finished. Processed: {0} blocks ({1} saved).", totalBlocks, savedBlocks.get());

        } catch (Exception ex) {
            CM2Logger.exception("[WERC] Error during batch logging", ex);
        }
    }
}
