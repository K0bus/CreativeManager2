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
    private final List<String> dependencies;

    private final Material icon;

    public Protection(String customId, Material icon, String... dependencies) {
        id = this.getClass().getSimpleName().replace("Protection", "").toLowerCase(Locale.getDefault());
        this.customId = customId;
        this.icon = icon;
        this.dependencies = List.of(dependencies);
    }

    public Protection(Material icon, String... dependencies) {
        this(null, icon, dependencies);
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
        for (String str : dependencies)
            if (!getPlugin().getServer().getPluginManager().isPluginEnabled(str)) return false;
        return true;
    }

    public List<String> missingDependencies() {
        List<String> missingDependencies = new ArrayList<>();
        for (String str : dependencies)
            if (!getPlugin().getServer().getPluginManager().isPluginEnabled(str)) missingDependencies.add(str);
        return missingDependencies;
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

    protected Protection init() {
        if (isCompatible()) {
            CreativeManager2.getAPI()
                    .getInstance()
                    .getServer()
                    .getPluginManager()
                    .registerEvents(this, CreativeManager2.getAPI().getInstance());
            loadSettings();
        }
        return this;
    }

    public static Map<String, Protection> loadProtections() {
        return Map.ofEntries(
                new AttackMonsterProtection().init().getMapEntry(),
                new AttackPlayerProtection().init().getMapEntry(),
                new BuildProtection().init().getMapEntry(),
                new ClaimProtection().init().getMapEntry(),
                new ContainerProtection().init().getMapEntry(),
                new CustomProtection().init().getMapEntry(),
                new DeathProtection().init().getMapEntry(),
                new DropProtection().init().getMapEntry(),
                new EffectProtection().init().getMapEntry(),
                new GuiProtection().init().getMapEntry(),
                new InventoryProtection().init().getMapEntry(),
                new ItemTrackProtection().init().getMapEntry(),
                new LogBlockProtection().init().getMapEntry(),
                new LogBlockPaperProtection().init().getMapEntry(),
                new LogEntityProtection().init().getMapEntry(),
                new PickupProtection().init().getMapEntry(),
                new ThrowProtection().init().getMapEntry(),
                new DataRemoverProtection().init().getMapEntry(),
                new ChestShopProtection().init().getMapEntry(),
                new ItemsAdderProtection().init().getMapEntry(),
                new SlimefunProtection().init().getMapEntry());
    }

    public static boolean isCreativePlayer(LivingEntity entity) {
        if (entity instanceof Player p) {
            return p.getGameMode().equals(GameMode.CREATIVE);
        }
        return false;
    }
}
