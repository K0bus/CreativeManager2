package fr.k0bus.creativemanager2.gui;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.Protection;
import fr.k0bus.creativemanager2.utils.GuiUtils;
import fr.k0bus.creativemanager2.utils.SpigotUtils;
import fr.k0bus.creativemanager2.utils.TextUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingGui extends PagedGui {
    public SettingGui(CreativeManager2 plugin) {
        super(3, "§9§lCreativeManager §7>> §r§4Settings", plugin);
        super.setSlots(GuiUtils.readIntArray(Collections.singletonList("0-17")));
    }

    @Override
    public SettingGui init() {
        super.init();
        clearContent();
        List<String> baseLore = new ArrayList<>();
        for (Protection protection : CreativeManager2.getAPI().getProtections().values()) {
            GuiItems guiItems = GuiItems.create(protection.getIcon());
            guiItems.setConsumer(inventoryClickEvent -> {
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
                    guiItems.setDisplayname(TextUtils.parseString(
                            "§7【§6§l\uD83D\uDD25§r§7】 §r§7" + TextUtils.proper(protection.getId())));
                    baseLore.clear();
                    baseLore.add(TextUtils.parseString("&l&cMissing plugins : "));
                    for (String str : missingDependencies) {
                        baseLore.add(TextUtils.parseString(" &7- &f" + str));
                    }
                    guiItems.setNewLore(baseLore);
                } else {
                    guiItems.setDisplayname(
                            TextUtils.parseString("§7【§c§l✘§r§7】 §r§7" + TextUtils.proper(protection.getId())));
                }
            } else {
                guiItems.setDisplayname(
                        TextUtils.parseString("§7【§a§l✔§r§7】 §r§f" + TextUtils.proper(protection.getId())));
            }
            if (SpigotUtils.isPaper())
                for (ItemFlag flag : ItemFlag.values()) {
                    guiItems.addItemFlags(flag);
                }
            else
                for (ItemFlag flag : ItemFlag.values()) {
                    ItemMeta meta = guiItems.getItemMeta();
                    if (meta == null) continue;
                    meta.addItemFlags(flag);
                    guiItems.setItemMeta(meta);
                }
            add(guiItems);
        }
        if (hasPreviousPage()) {
            GuiItems pagePrevious = new GuiItems(Material.ARROW, (e) -> {
                previous();
                init();
            });
            pagePrevious.setDisplayname(TextUtils.parseString("&6Previous page"));
            setItem(20, pagePrevious);
        }
        if (hasNextPage()) {
            GuiItems pageNext = new GuiItems(Material.ARROW, (e) -> {
                next();
                init();
            });
            pageNext.setDisplayname(TextUtils.parseString("&6Next page"));
            setItem(24, pageNext);
        }

        GuiItems closeItem = new GuiItems(Material.BARRIER, (e) -> {
            for (HumanEntity entity : new ArrayList<>(this.getInventory().getViewers())) {
                entity.closeInventory();
            }
        });
        closeItem.setDisplayname(TextUtils.parseString("&cClose menu"));
        setItem(getInventory().getSize() - 1, closeItem);
        drawContent();
        return this;
    }
}
