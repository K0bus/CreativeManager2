package fr.k0bus.creativemanager2.file.language;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import fr.k0bus.creativemanager2.CM2Logger;
import fr.k0bus.creativemanager2.CreativeManager2;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinecraftLang {

    private final String mcVersion;
    private final String lang;

    JsonObject jsonObject;

    public MinecraftLang(JavaPlugin plugin, String lang, String mcVersion) {
        this.mcVersion = mcVersion.toLowerCase(Locale.getDefault());
        this.lang = lang.toLowerCase(Locale.getDefault());
        loadLang(plugin);
    }

    public MinecraftLang(JavaPlugin plugin, String lang) {
        this(plugin, lang, Bukkit.getBukkitVersion().split("-")[0]);
    }

    private void loadLang(JavaPlugin plugin) {
        String fileUrl = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/"
                + mcVersion.toLowerCase(Locale.getDefault())
                + "/assets/minecraft/lang/"
                + lang.toLowerCase(Locale.getDefault())
                + ".json";
        Path dir = plugin.getDataFolder().toPath().resolve("locale");

        try {
            Files.createDirectories(dir); // Crée le dossier s'il n'existe pas
        } catch (IOException e) {
            CM2Logger.exception(new Exception("Can't create MC Lang directory", e));
            CreativeManager2.getAPI().disableCM2();
            return;
        }

        Path localeFile = dir.resolve(lang.toLowerCase(Locale.getDefault()) + ".json");

        if (Files.notExists(localeFile)) {
            CM2Logger.info("   &7> &6&lStarting downloading {0}.json on version {1}", lang, mcVersion);

            try (InputStream in = new URI(fileUrl).toURL().openStream();
                    OutputStream out = Files.newOutputStream(localeFile)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead = in.read(dataBuffer, 0, 1024);
                while (bytesRead != -1) {
                    out.write(dataBuffer, 0, bytesRead);
                    bytesRead = in.read(dataBuffer, 0, 1024); // Ré-assigner bytesRead à chaque itération
                }
            } catch (IOException | URISyntaxException e) {
                if (CreativeManager2.getAPI() != null) {
                    CM2Logger.warn("   &7> &c&lCan't download locale file {0}.json", lang);
                    CM2Logger.warn("   &7> &c&l - URL : {0}", fileUrl);
                    String absolutePath = localeFile.toAbsolutePath().toString();
                    CM2Logger.warn("   &7> &c&l - Destination : {0}", absolutePath);
                }
                return;
            }
        }

        if (Files.exists(localeFile)) {
            CM2Logger.info("   &7> &6&lLoading file {0}.json", lang);
            Gson gson = new Gson();
            try (BufferedReader reader = Files.newBufferedReader(localeFile, StandardCharsets.UTF_8)) {
                jsonObject = gson.fromJson(reader, JsonObject.class);
            } catch (IOException e) {
                CM2Logger.exception(e);
            }

            if (jsonObject == null) {
                CM2Logger.warn("   &7> &c&lFile not loaded successfully !");
            } else {
                CM2Logger.info("   &7> &a&lFile loaded successfully !");
            }
        }
    }

    public String get(String k) {
        if (jsonObject == null) return "null";
        if (jsonObject.get(k) == null) return "not contain";
        return jsonObject.get(k).getAsString();
    }

    public String get(Material material) {
        return get(MinecraftLangKey.getTranslationKey(material));
    }

    public String get(Effect effect) {
        return get(MinecraftLangKey.getTranslationKey(effect));
    }

    public String get(Enchantment enchantment) {
        return get(MinecraftLangKey.getTranslationKey(enchantment));
    }

    public String get(EntityType entityType) {
        return get(MinecraftLangKey.getTranslationKey(entityType));
    }

    public String get(Statistic statistic) {
        return get(MinecraftLangKey.getTranslationKey(statistic));
    }

    public String get(ItemStack itemStack) {
        return get(MinecraftLangKey.getTranslationKey(itemStack));
    }
}
