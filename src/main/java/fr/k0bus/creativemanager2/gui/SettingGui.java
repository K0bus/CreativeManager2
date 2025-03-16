package fr.k0bus.creativemanager2.gui;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.Serializer;
import fr.k0bus.creativemanager2.utils.StringUtils;
import fr.k0bus.creativemanager2.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingGui extends PagedMenu {
    public SettingGui(CreativeManager2 plugin) {
        super(
                3,
                "§9§lCreativeManager §7>> §r§4Settings",
                plugin,
                CreativeManager2.getAPI().getMenuListener());
        super.setSlots(Serializer.readIntArray(Collections.singletonList("0-17")));
    }

    @Override
    public SettingGui init() {
        clearContent();
        List<String> baseLore = new ArrayList<>();
        for (Protection protection : CreativeManager2.getAPI().getProtections().values()) {
            MenuItems menuItems = MenuItems.create(protection.getIcon());
            menuItems.setConsumer(inventoryClickEvent -> {
                protection.setEnabled(protection.isDisabled());
                CreativeManager2.getAPI()
                        .getSettings()
                        .set("protections." + protection.getCustomId() + ".enabled", true);
                CreativeManager2.getAPI().getSettings().save();
                init();
            });
            if (protection.isDisabled()) {
                List<String> missingDependencies = protection.missingDependencies();
                if (!missingDependencies.isEmpty()) {
                    menuItems.setDisplayname(StringUtils.translateColor(
                            "§7【§6§l\uD83D\uDD25§r§7】 §r§7" + StringUtils.proper(protection.getId())));
                    baseLore.clear();
                    baseLore.add(StringUtils.translateColor("&l&cMissing plugins : "));
                    for (String str : missingDependencies) {
                        baseLore.add(StringUtils.translateColor(" &7- &f" + str));
                    }
                    menuItems.setNewLore(baseLore);
                } else {
                    menuItems.setDisplayname(
                            StringUtils.translateColor("§7【§c§l✘§r§7】 §r§7" + StringUtils.proper(protection.getId())));
                }
            } else {
                menuItems.setDisplayname(
                        StringUtils.translateColor("§7【§a§l✔§r§7】 §r§f" + StringUtils.proper(protection.getId())));
            }
            if (Utils.isPaper())
                for (ItemFlag flag : ItemFlag.values()) {
                    menuItems.addItemFlags(flag);
                }
            else
                for (ItemFlag flag : ItemFlag.values()) {
                    ItemMeta meta = menuItems.getItemMeta();
                    if (meta == null) continue;
                    meta.addItemFlags(flag);
                    menuItems.setItemMeta(meta);
                }
            add(menuItems);
        }
        if (hasPreviousPage()) {
            MenuItems pagePrevious = new MenuItems(Material.ARROW, (e) -> {
                previous();
                init();
            });
            pagePrevious.setDisplayname(StringUtils.translateColor("&6Previous page"));
            setItem(20, pagePrevious);
        }
        if (hasNextPage()) {
            MenuItems pageNext = new MenuItems(Material.ARROW, (e) -> {
                next();
                init();
            });
            pageNext.setDisplayname(StringUtils.translateColor("&6Next page"));
            setItem(24, pageNext);
        }

        MenuItems closeItem = new MenuItems(Material.BARRIER, (e) -> {
            for (HumanEntity entity : new ArrayList<>(this.getInventory().getViewers())) {
                entity.closeInventory();
            }
        });
        closeItem.setDisplayname(StringUtils.translateColor("&cClose menu"));
        setItem(getInventory().getSize() - 1, closeItem);
        drawContent();
        return this;
    }
}
