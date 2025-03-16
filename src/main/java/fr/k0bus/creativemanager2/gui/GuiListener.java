package fr.k0bus.creativemanager2.gui;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class GuiListener implements Listener {

    private final Map<Inventory, Gui> menuMap = new HashMap<>();

    public void add(Gui gui) {
        if (menuMap.containsKey(gui.getInventory())) return;
        menuMap.put(gui.getInventory(), gui);
    }

    public void remove(Gui gui) {
        if (!menuMap.containsKey(gui.getInventory())) return;
        if (!gui.getInventory().getViewers().isEmpty()) return;
        menuMap.remove(gui.getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onClick(e);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onDrag(e);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onOpen(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onClose(e);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent e) {
        if (menuMap.containsKey(e.getInventory())) {
            menuMap.get(e.getInventory()).onInteract(e);
        }
    }
}
