package fr.k0bus.creativemanager2.protections;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import java.util.Locale;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;

public abstract class Protection implements Listener {

    private final String id;
    private boolean enabled = true;
    protected ConfigurationSection config;
    private final CreativeManager2 plugin;
    private final String customId;

    private final Material icon;

    public Protection(CreativeManager2 plugin, Material icon, String customId) {
        id = this.getClass().getSimpleName().replace("Protection", "").toLowerCase(Locale.getDefault());
        this.plugin = plugin;
        this.customId = customId;
        this.icon = icon;
    }

    public Protection(CreativeManager2 plugin, Material icon) {
        this(plugin, icon, null);
    }

    public void loadSettings() {
        if (!CreativeManager2.getAPI().getSettings().getBoolean("protections." + getCustomId() + ".enabled")) {
            setEnabled(false);
        }
        if (CreativeManager2.getAPI().getSettings().contains("protections." + getCustomId())) {
            config = CreativeManager2.getAPI().getSettings().getConfigurationSection("protections." + getCustomId());
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
        CM2Utils.sendMessage(toMessage, "permission." + getId());
    }

    public void sendPermissionMessage(CommandSender toMessage, String custom) {
        CM2Utils.sendMessage(toMessage, "permission." + getId() + "." + custom);
    }

    public Material getIcon() {
        return icon;
    }

    public ConfigurationSection getConfig() {
        return config;
    }

    public String getCustomId() {
        if (customId == null) return id;
        else return customId;
    }
}
