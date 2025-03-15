package fr.k0bus.creativemanager2.file;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.Settings;
import fr.k0bus.creativemanager2.CM2Logger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CM2YamlDocument extends YamlDocument {
    protected CM2YamlDocument(@NotNull File document, @Nullable InputStream defaults, @NotNull Settings... settings)
            throws IOException {
        super(document, defaults, settings);
    }

    @Override
    public boolean save() {
        try {
            super.save();
            return true;
        } catch (IOException e) {
            CM2Logger.exception(e);
        }
        return false;
    }

    @Override
    public boolean reload() {
        try {
            super.reload();
            return true;
        } catch (IOException e) {
            CM2Logger.exception(e);
        }
        return false;
    }
}
