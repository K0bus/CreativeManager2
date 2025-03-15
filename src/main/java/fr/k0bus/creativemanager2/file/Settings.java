package fr.k0bus.creativemanager2.file;

import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.io.File;
import java.io.IOException;

public class Settings extends CM2YamlDocument {

    public Settings() throws IOException {
        super(
                new File(CreativeManager2.getAPI().getInstance().getDataFolder(), "config.yml"),
                CreativeManager2.getAPI().getInstance().getResource("config.yml"),
                GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build());
    }

    public String getLang() {
        return getString("plugin.lang");
    }

    public String getTag() {
        return getString("plugin.tag");
    }

    public boolean debugMode() {
        return getBoolean("plugin.debug-mode");
    }
}
