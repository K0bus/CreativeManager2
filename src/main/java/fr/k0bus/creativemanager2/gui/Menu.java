package fr.k0bus.creativemanager2.gui;

import fr.k0bus.creativemanager2.utils.Utils;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public abstract class Menu {

    private final Inventory inventory;
    private final HashMap<Integer, MenuItems> menuItemsHashMap = new HashMap<>();

    private final JavaPlugin plugin;

    private final MenuListener listener;

    @SuppressWarnings("deprecation")
    public Menu(int size, String name, JavaPlugin plugin, MenuListener listener) {
        inventory = Bukkit.createInventory(null, 9 * size, Utils.placeholderApiParse(name));
        this.plugin = plugin;
        this.listener = listener;
        listener.add(this);
    }

    public Menu(String name, JavaPlugin pl, MenuListener listener) {
        this(6, name, pl, listener);
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract Menu init();

    public void setItem(int slot, MenuItems menuItems) {
        inventory.setItem(slot, menuItems);
        menuItemsHashMap.put(slot, menuItems);
    }

    public void open(Player p) {
        p.openInventory(inventory);
    }

    public void close(Player p) {
        if (p.getOpenInventory().getTopInventory().equals(inventory)) p.closeInventory();
        listener.remove(this);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void onClick(InventoryClickEvent e) {
        if (menuItemsHashMap.containsKey(e.getSlot())) {
            MenuItems menuItems = menuItemsHashMap.get(e.getSlot());
            if (menuItems != null) menuItems.onClick(e);
        }
    }

    public void onDrag(InventoryDragEvent e) {
        e.setCancelled(true);
    }

    public abstract void onOpen(InventoryOpenEvent e);

    public abstract void onClose(InventoryCloseEvent e);

    public abstract void onInteract(InventoryInteractEvent e);
}
