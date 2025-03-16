package fr.k0bus.creativemanager2.file.language;

import java.util.Locale;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class MinecraftLangKey {
    public static String getTranslationKey(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        Material m = itemStack.getType();

        /* Check for Potions / Tiped Arrow */
        if (itemMeta instanceof PotionMeta potionMeta) {
            if (!potionMeta.hasCustomEffects()) return getTranslationKey(m);
            if (!potionMeta.getCustomEffects().isEmpty()) {
                return getTranslationKey(m, potionMeta.getCustomEffects().getFirst());
            } else {
                PotionType potionType = potionMeta.getBasePotionType();
                if (potionType != null)
                    return getTranslationKey(m, potionType.getPotionEffects().getFirst());
            }
        }

        return getTranslationKey(m);
    }

    public static String getTranslationKey(@NotNull Material m) {
        String type = "item";
        if (m.isBlock()) type = "block";
        return type + ".minecraft." + m.name().toLowerCase(Locale.getDefault());
    }

    public static String getTranslationKey(Enchantment e) {
        return "enchantment.minecraft." + e.getKey().getKey().toLowerCase(Locale.getDefault());
    }

    public static String getTranslationKey(EntityType e) {
        return "entity.minecraft." + e.name().toLowerCase(Locale.getDefault());
    }

    public static String getTranslationKey(Effect e) {
        return "effect.minecraft." + e.name().toLowerCase(Locale.getDefault());
    }

    public static String getTranslationKey(Statistic s) {
        return "stat.minecraft." + s.name().toLowerCase(Locale.getDefault());
    }

    private static String getTranslationKey(Material m, PotionEffect potionEffect) {
        return "item.minecraft."
                + m.name().toLowerCase(Locale.getDefault())
                + ".effect."
                + potionEffect.getType().translationKey().toLowerCase(Locale.getDefault());
    }
}
