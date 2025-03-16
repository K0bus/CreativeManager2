package fr.k0bus.creativemanager2.gui;

import fr.k0bus.creativemanager2.utils.SpigotUtils;
import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiItems extends ItemStack {

    private Consumer<InventoryClickEvent> consumer;
    private Sound sound;

    public GuiItems(Material m, int size) {
        super(m, size);
        try {
            sound = Sound.UI_BUTTON_CLICK;
        } catch (Error ignored) {
        }
    }

    public GuiItems(Material m, int size, Consumer<InventoryClickEvent> consumer) {
        this(m, size);
        this.consumer = consumer;
    }

    public GuiItems(Material m, Consumer<InventoryClickEvent> consumer) {
        this(m, 1, consumer);
    }

    public void setConsumer(Consumer<InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }

    public void setNewLore(List<String> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            SpigotUtils.setItemMetaLore(itemMeta, lore);
            setItemMeta(itemMeta);
        }
    }

    public void setDisplayname(String str) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta != null) {
            SpigotUtils.setItemMetaDisplayname(itemMeta, str);
            setItemMeta(itemMeta);
        }
    }

    public boolean isClickable() {
        return this.consumer != null;
    }

    public void onClick(InventoryClickEvent e) {
        if (!isClickable()) return;
        if (!(e.getWhoClicked() instanceof Player p)) return;
        if (sound != null) p.playSound(p.getLocation(), sound, 0.5f, 1);
        consumer.accept(e);
    }

    public static GuiItems create(Material material) {
        return new GuiItems(material, 1);
    }
}
