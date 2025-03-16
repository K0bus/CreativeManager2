package fr.k0bus.creativemanager2.gui;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.utils.SpigotUtils;
import fr.k0bus.creativemanager2.utils.TextUtils;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public abstract class Gui {

    private final Inventory inventory;
    private final Map<Integer, GuiItems> menuItemsHashMap = new HashMap<>();
    private final JavaPlugin plugin;

    public Gui(int size, String name, JavaPlugin plugin) {
        inventory = SpigotUtils.createInventory(size, TextUtils.placeholderApiParse(name));
        this.plugin = plugin;
    }

    public Gui(String name, JavaPlugin pl) {
        this(6, name, pl);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public Gui init() {
        CreativeManager2.getAPI().registerGui(this);
        return this;
    }

    public void setItem(int slot, GuiItems guiItems) {
        inventory.setItem(slot, guiItems);
        menuItemsHashMap.put(slot, guiItems);
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public void close(Player p) {
        if (p.getOpenInventory().getTopInventory().equals(inventory)) p.closeInventory();
        CreativeManager2.getAPI().unregisterGui(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent e) {
        if (menuItemsHashMap.containsKey(e.getSlot())) {
            GuiItems guiItems = menuItemsHashMap.get(e.getSlot());
            if (guiItems != null) guiItems.onClick(e);
        }
    }

    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    public abstract void onOpen(InventoryOpenEvent e);

    public abstract void onClose(InventoryCloseEvent e);

    public abstract void onInteract(InventoryInteractEvent e);
}
