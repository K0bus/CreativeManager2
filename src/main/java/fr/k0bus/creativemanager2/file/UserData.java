package fr.k0bus.creativemanager2.file;

import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.io.File;
import java.io.IOException;
import org.bukkit.entity.Player;

public class UserData extends CM2YamlDocument {

    public UserData(Player p) throws IOException {
        super(
                new File(CreativeManager2.getAPI().getInstance().getDataFolder() + "/data", p.getUniqueId() + ".yml"),
                CreativeManager2.getAPI().getInstance().getResource(p.getUniqueId() + ".yml"),
                GeneralSettings.builder()
                        .setSerializer(SpigotSerializer.getInstance())
                        .build());
    }
}
