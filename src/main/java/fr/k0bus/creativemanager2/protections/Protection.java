package fr.k0bus.creativemanager2.protections;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.protections.addons.ChestShopProtection;
import fr.k0bus.creativemanager2.protections.addons.ItemsAdderProtection;
import fr.k0bus.creativemanager2.protections.addons.SlimefunProtection;
import fr.k0bus.creativemanager2.protections.generic.*;
import fr.k0bus.creativemanager2.utils.MessageUtils;
import java.util.*;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class Protection implements Listener {

    private final String id;
    private boolean enabled = true;
    protected Section config;
    private final CreativeManager2 plugin = CreativeManager2.getAPI().getInstance();
    private final String customId;

    private final Material icon;

    public Protection(Material icon, String customId) {
        id = this.getClass().getSimpleName().replace("Protection", "").toLowerCase(Locale.getDefault());
        this.customId = customId;
        this.icon = icon;
    }

    public Protection(Material icon) {
        this(icon, null);
    }

    public void loadSettings() {
        if (!CreativeManager2.getAPI().getSettings().getBoolean("protections." + getCustomId() + ".enabled")) {
            setEnabled(false);
        }
        if (CreativeManager2.getAPI().getSettings().contains("protections." + getCustomId())) {
            config = CreativeManager2.getAPI().getSettings().getSection("protections." + getCustomId());
        }
    }

    public boolean hasPermission(LivingEntity player) {
        return player.hasPermission(getPermission());
    }

    public boolean hasPermission(LivingEntity player, String permission) {
        return player.hasPermission(getPermission() + "." + permission);
    }

    public String getPermission() {
        return "creativemanager." + getCustomId();
    }

    public String getId() {
        return id;
    }

    public CreativeManager2 getPlugin() {
        return plugin;
    }

    public boolean isDisabled() {
        return !enabled || !isCompatible();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isCompatible() {
        return true;
    }

    public void sendPermissionMessage(CommandSender toMessage) {
        MessageUtils.sendMessage(toMessage, "permission." + getId());
    }

    public void sendPermissionMessage(CommandSender toMessage, String custom) {
        MessageUtils.sendMessage(toMessage, "permission." + getId() + "." + custom);
    }

    public Material getIcon() {
        return icon;
    }

    public Section getConfig() {
        return config;
    }

    public String getCustomId() {
        if (customId == null) return id;
        else return customId;
    }

    protected Map.Entry<String, Protection> getMapEntry() {
        return new AbstractMap.SimpleEntry<>(getCustomId(), this);
    }

    public static Map<String, Protection> loadProtections() {
        return Map.ofEntries(
                new AttackMonsterProtection().getMapEntry(),
                new AttackPlayerProtection().getMapEntry(),
                new BuildProtection().getMapEntry(),
                new ClaimProtection().getMapEntry(),
                new ContainerProtection().getMapEntry(),
                new CustomProtection().getMapEntry(),
                new DeathProtection().getMapEntry(),
                new DropProtection().getMapEntry(),
                new EffectProtection().getMapEntry(),
                new EnchantProtection().getMapEntry(),
                new GuiProtection().getMapEntry(),
                new InventoryProtection().getMapEntry(),
                new ItemTrackProtection().getMapEntry(),
                new LogBlockProtection().getMapEntry(),
                new LogEntityProtection().getMapEntry(),
                new PickupProtection().getMapEntry(),
                new ThrowProtection().getMapEntry(),
                new ChestShopProtection().getMapEntry(),
                new ItemsAdderProtection().getMapEntry(),
                new SlimefunProtection().getMapEntry());
    }

    public static boolean isCreativePlayer(LivingEntity entity) {
        if (entity instanceof Player p) {
            return p.getGameMode().equals(GameMode.CREATIVE);
        }
        return false;
    }
}
