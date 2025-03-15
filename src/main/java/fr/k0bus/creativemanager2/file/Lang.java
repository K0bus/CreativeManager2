package fr.k0bus.creativemanager2.file;

import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.io.File;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

public class Lang extends CM2YamlDocument {


    public Lang(@NotNull String langString) throws IOException {
        super(
                new File(CreativeManager2.getAPI().getInstance().getDataFolder() + "/lang", langString + ".yml"),
                CreativeManager2.getAPI().getInstance().getResource(langString + ".yml"),
                GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build());
    }
}
