package fr.k0bus.creativemanager2.gui;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class MenuListener implements Listener {

    private final HashMap<Inventory, Menu> menuMap = new HashMap<>();

    public void add(Menu menu)
    {
        if(menuMap.containsKey(menu.getInventory())) return;
        menuMap.put(menu.getInventory(), menu);
    }
    public void remove(Menu menu)
    {
        if(!menuMap.containsKey(menu.getInventory())) return;
        if(!menu.getInventory().getViewers().isEmpty()) return;
        menuMap.remove(menu.getInventory());
    }


    @EventHandler
    public void onClick(InventoryClickEvent e)
    {
        if(menuMap.containsKey(e.getInventory()))
        {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onClick(e);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e)
    {
        if(menuMap.containsKey(e.getInventory()))
        {
            e.setCancelled(true);
            menuMap.get(e.getInventory()).onDrag(e);
        }
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e)
    {
        if(menuMap.containsKey(e.getInventory()))
        {
            menuMap.get(e.getInventory()).onOpen(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e)
    {
        if(menuMap.containsKey(e.getInventory()))
        {
            menuMap.get(e.getInventory()).onClose(e);
        }
    }

    @EventHandler
    public void onInteract(InventoryInteractEvent e)
    {
        if(menuMap.containsKey(e.getInventory()))
        {
            menuMap.get(e.getInventory()).onInteract(e);
        }
    }
}
