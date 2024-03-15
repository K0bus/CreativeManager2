package fr.k0bus.creativemanager2.protections.generic;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.land.Area;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Set;

public class ClaimProtection extends Protection {
    public ClaimProtection(CreativeManager2 plugin) {
        super(plugin, Material.OAK_SIGN);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if(notMember(event.getPlayer(), event.getBlock().getLocation())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        if(notMember(event.getPlayer(), event.getBlock().getLocation())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    private boolean notMember(Player player, Location location)
    {
        if(getPlugin().getServer().getPluginManager().isPluginEnabled("WorldGuard"))
        {
            if(isMemberWG(player, location)) return false;
        }
        if(getPlugin().getServer().getPluginManager().isPluginEnabled("Lands"))
        {
            if(isMemberLands(player, location)) return false;
        }
        if(getPlugin().getServer().getPluginManager().isPluginEnabled("GriefPrevention"))
        {
            if(isMemberGP(player, location)) return false;
        }
        return true;
    }

    private boolean isMemberWG(Player player, Location location)
    {
        LocalPlayer localPlayer = WorldGuardPlugin.inst().wrapPlayer(player);
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        Set<ProtectedRegion> regions = query.getApplicableRegions(BukkitAdapter.adapt(location)).getRegions();
        for (ProtectedRegion region:regions) {
            if(region.isMember(localPlayer) && query.testState(BukkitAdapter.adapt(location), localPlayer, Flags.BUILD)) return true;
        }
        return false;
    }
    private boolean isMemberLands(Player player, Location location)
    {
        LandsIntegration api = LandsIntegration.of(getPlugin());
        Area area = api.getArea(location);
        if(area != null)
            return area.isTrusted(player.getUniqueId());
        return false;
    }

    private boolean isMemberGP(Player player, Location location)
    {
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
        if(claim != null)
            return claim.getOwnerID().equals(player.getUniqueId());
        return false;
    }
}
