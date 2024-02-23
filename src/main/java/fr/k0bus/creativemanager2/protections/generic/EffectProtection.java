package fr.k0bus.creativemanager2.protections.generic;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.potion.PotionEffect;

public class EffectProtection extends Protection {
    public EffectProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGMChange(PlayerGameModeChangeEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        for (PotionEffect effect: event.getPlayer().getActivePotionEffects()) {
            event.getPlayer().removePotionEffect(effect.getType());
        }
    }
}
