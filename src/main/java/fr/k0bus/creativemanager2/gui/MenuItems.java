package fr.k0bus.creativemanager2.gui;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;

public class MenuItems extends ItemStack {

    private Consumer<InventoryClickEvent> consumer;
    private Sound sound;

    public MenuItems(Material m, int size)
    {
        super(m, size);
        try {
            sound = Sound.UI_BUTTON_CLICK;
        }
        catch (Error ignored) {}
    }
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer)
    {
        this(m, size);
        this.consumer = consumer;
    }
    public MenuItems(Material m, int size, Consumer<InventoryClickEvent> consumer, Sound sound)
    {
        this(m, size, consumer);
        this.sound = sound;
    }
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer)
    {
        this(m, 1, consumer);
    }
    public MenuItems(Material m, Consumer<InventoryClickEvent> consumer, Sound sound)
    {
        this(m, 1, consumer, sound);
    }

    public MenuItems(ItemStack itemStack, Consumer<InventoryClickEvent> consumer, Sound sound)
    {
        this(itemStack);
        setConsumer(consumer);
        setSound(sound);
    }
    public MenuItems(ItemStack itemStack, Consumer<InventoryClickEvent> consumer)
    {
        this(itemStack);
        setConsumer(consumer);
    }
    public MenuItems(ItemStack itemStack)
    {
        super(itemStack);
        try {
            sound = Sound.UI_BUTTON_CLICK;
        }
        catch (Error ignored) {}
    }

    public void setConsumer(Consumer<InventoryClickEvent> consumer) {
        this.consumer = consumer;
    }

    public void setSound(Sound sound) {
        this.sound = sound;
    }

    public void setLore(List<String> lore)
    {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null)
        {
            itemMeta.setLore(lore);
            setItemMeta(itemMeta);
        }
    }
    public void setDisplayname(String str)
    {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null)
        {
            itemMeta.setDisplayName(str);
            setItemMeta(itemMeta);
        }
    }
    public void setModel(int model)
    {
        ItemMeta itemMeta = getItemMeta();
        if(itemMeta != null)
        {
            itemMeta.setCustomModelData(model);
            setItemMeta(itemMeta);
        }
    }

    public boolean isClickable() {
        return this.consumer != null;
    }

    public void onClick(InventoryClickEvent e)
    {
        if(!isClickable()) return;
        if(!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();
        if(sound != null)
            p.playSound(p.getLocation(), sound, 0.5f, 1);
        consumer.accept(e);
    }
}
