package fr.k0bus.creativemanager2.gui;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PagedMenu extends Menu {

    int page;
    int[] slots;
    List<MenuItems> content = new ArrayList<>();

    public PagedMenu(int size, String name, JavaPlugin plugin, MenuListener listener) {
        super(size, name, plugin, listener);
    }

    @Override
    public PagedMenu init() {
        getInventory().clear();
        return this;
    }

    @Override
    public void onOpen(InventoryOpenEvent e) {}

    @Override
    public void onClose(InventoryCloseEvent e) {}

    @Override
    public void onInteract(InventoryInteractEvent e) {}

    public boolean hasNextPage() {
        return page < getMaxPage();
    }

    public boolean hasPreviousPage() {
        return page > 0;
    }

    public void next() {
        if (hasNextPage()) {
            page++;
            drawContent();
        }
    }

    public void previous() {
        if (hasPreviousPage()) {
            page--;
            drawContent();
        }
    }

    public void setSlots(int... slots) {
        this.slots = slots.clone();
    }

    public void add(MenuItems menuItems) {
        this.content.add(menuItems);
    }

    public void clearInventoryContent() {
        for (int n : slots) {
            setItem(n, null);
        }
    }

    public void clearContent() {
        clearInventoryContent();
        content = new ArrayList<>();
    }

    public void drawContent() {
        clearInventoryContent();
        int i = page * slots.length;
        for (int n : slots) {
            if (i < content.size()) {
                setItem(n, content.get(i));
            }
            i++;
        }
    }

    public int getMaxPage() {
        return Math.max(((int) Math.ceil((double) content.size() / (double) slots.length)) - 1, 1);
    }
}
