package fr.k0bus.creativemanager2.utils.language;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MinecraftLang {


    private final String mcVersion;
    private final String lang;

    JsonObject jsonObject;

    public MinecraftLang(JavaPlugin plugin, String lang, String mcVersion)
    {
        this.mcVersion = mcVersion.toLowerCase();
        this.lang = lang.toLowerCase();
        loadLang(plugin);
    }
    public MinecraftLang(JavaPlugin plugin, String lang)
    {
        this(plugin, lang, Bukkit.getBukkitVersion().split("-")[0]);
    }

    private void loadLang(JavaPlugin plugin)
    {
        String FILE_URL = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/" + mcVersion.toLowerCase() + "/assets/minecraft/lang/" + lang.toLowerCase() + ".json";
        File dir = new File(plugin.getDataFolder(), "locale");
        if(!dir.exists()) dir.mkdir();
        File localeFile = new File(dir, lang.toLowerCase() + ".json");
        if(!localeFile.exists()) {
            plugin.getLogger().info("§Starting downloading " + lang + ".json on version" + mcVersion.toLowerCase());
            try (BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(localeFile)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                // handle exception
                plugin.getLogger().warning("§Can't download locale file " + lang + ".json");
                plugin.getLogger().warning("§URL : " + FILE_URL);
                plugin.getLogger().warning("§cDestination : " + localeFile.getPath());
            }
        }
        if(localeFile.exists())
        {
            plugin.getLogger().info("§2Loading file " + this.lang + ".json");
            Gson gson = new Gson();
            try {
                jsonObject = gson.fromJson(new FileReader(localeFile, StandardCharsets.UTF_8), JsonObject.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(jsonObject == null)
                plugin.getLogger().warning("§cFile not loaded successfully !");
            else
                plugin.getLogger().info("§2File loaded successfully !");
        }
    }

    public String get(String k)
    {
        if(jsonObject == null) return "null";
        if(jsonObject.get(k) == null) return "not contain";
        return jsonObject.get(k).getAsString();
    }

    public String get(Material material)
    {
        return get(MinecraftLangKey.getTranslationKey(material));
    }
    public String get(Effect effect)
    {
        return get(MinecraftLangKey.getTranslationKey(effect));
    }
    public String get(Enchantment enchantment)
    {
        return get(MinecraftLangKey.getTranslationKey(enchantment));
    }
    public String get(EntityType entityType)
    {
        return get(MinecraftLangKey.getTranslationKey(entityType));
    }
    public String get(Statistic statistic)
    {
        return get(MinecraftLangKey.getTranslationKey(statistic));
    }
    public String get(ItemStack itemStack)
    {
        return get(MinecraftLangKey.getTranslationKey(itemStack));
    }
}
