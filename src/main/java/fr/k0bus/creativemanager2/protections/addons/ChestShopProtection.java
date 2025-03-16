package fr.k0bus.creativemanager2.protections.addons;

import com.Acrobot.ChestShop.Events.PreShopCreationEvent;
import com.Acrobot.ChestShop.Events.PreTransactionEvent;
import fr.k0bus.creativemanager2.protections.Protection;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class ChestShopProtection extends Protection {
    public ChestShopProtection() {
        super(Material.GOLD_NUGGET);
        addDependencies("ChestShop");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShopCreation(PreShopCreationEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getPlayer())) return;
        if (!Protection.isCreativePlayer(event.getPlayer())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onShopTransaction(PreTransactionEvent event) {
        if (isDisabled()) return;
        if (hasPermission(event.getClient())) return;
        if (!Protection.isCreativePlayer(event.getClient())) return;
        event.setCancelled(true);
        sendPermissionMessage(event.getClient());
    }
}
