package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AttackProtection extends Protection {
    public AttackProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if(isDisabled()) return;
        if(!(event.getDamager() instanceof Player player)) return;
        if(hasPermission(player)) return;
        if(!CM2Utils.isCreativePlayer(player)) return;
        event.setCancelled(true);
        sendPermissionMessage(player);
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileAttack(ProjectileHitEvent event)
    {
        if(isDisabled()) return;
        if(!(event.getEntity().getShooter() instanceof Player player)) return;
        if(hasPermission(player)) return;
        if(!CM2Utils.isCreativePlayer(player)) return;
        event.setCancelled(true);
        sendPermissionMessage(player);
    }
}
