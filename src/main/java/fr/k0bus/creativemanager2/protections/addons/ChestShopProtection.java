package fr.k0bus.creativemanager2.protections.addons;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import fr.k0bus.creativemanager2.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ChestShopProtection extends Protection {
    public ChestShopProtection(CreativeManager2 plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShopCreation(PreShopCreationEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getPlayer())) return;
        if(!CM2Utils.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        //TODO: Message sender
    }
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShopTransaction(PreTransactionEvent event)
    {
        if(isDisabled()) return;
        if(hasPermission(event.getClient())) return;
        if(!CM2Utils.isCreativePlayer(event.getClient())) return;
        event.setCancelled(true);
        //TODO: Message sender
    }
}