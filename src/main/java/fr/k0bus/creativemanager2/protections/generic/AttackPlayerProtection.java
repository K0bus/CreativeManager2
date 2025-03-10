package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class AttackPlayerProtection extends Protection {
    public AttackPlayerProtection(CreativeManager2 plugin) {
        super(plugin, Material.GOLDEN_SWORD, "combat.pvp");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAttack(EntityDamageByEntityEvent event)
    {
        if(isDisabled()) return;
        if(!(event.getDamager() instanceof Player player)) return;
        if(!(event.getEntity() instanceof Player)) return;
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
        if(!(event.getHitEntity() instanceof Player)) return;
        if(hasPermission(player)) return;
        if(!CM2Utils.isCreativePlayer(player)) return;
        event.setCancelled(true);
        sendPermissionMessage(player);
    }
}
