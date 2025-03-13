package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nullable;
import java.util.UUID;

public class CM2Data {

    private static final String UUID_ID = "/UUID";
    private static final String DATE_ID = "/DATE";

    public static void register(Block block, Player player)
    {
        register(block.getLocation(), player);
    }
    public static void register(Block block, UUID uuid)
    {
        register(block.getLocation(), uuid);
    }
    public static void register(Location location, Player player)
    {
        register(location, player.getUniqueId());
    }
    public static void register(Location location, UUID uuid)
    {
        String serializedLocation = serializeLocation(location);
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + UUID_ID);
        location.getChunk().getPersistentDataContainer()
                .set(namespacedKeyUuid, PersistentDataType.STRING, uuid.toString());
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + DATE_ID);
        location.getChunk().getPersistentDataContainer()
                .set(namespacedKeyDate, PersistentDataType.LONG, System.currentTimeMillis());
    }

    public static void register(Entity entity, Player player)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), UUID_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyUuid, PersistentDataType.STRING, player.getUniqueId().toString());
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), DATE_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyDate, PersistentDataType.LONG, System.currentTimeMillis());
    }
    public static void register(Entity entity, UUID uuid)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), UUID_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyUuid, PersistentDataType.STRING, uuid.toString());
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), DATE_ID);
        entity.getPersistentDataContainer()
                .set(namespacedKeyDate, PersistentDataType.LONG, System.currentTimeMillis());
    }
    public static void unregister(Entity entity)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), UUID_ID);
        entity.getPersistentDataContainer().remove(namespacedKeyUuid);
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), DATE_ID);
        entity.getPersistentDataContainer().remove(namespacedKeyDate);
    }
    @Nullable
    public static UUID findPlayer(Entity entity)
    {
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), UUID_ID);
        String UUIDText = entity.getPersistentDataContainer().get(namespacedKeyUuid, PersistentDataType.STRING);
        if(UUIDText == null)
            return null;
        return UUID.fromString(UUIDText);
    }
    public static long findDate(Entity entity)
    {
        if(entity == null) return 0;
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), DATE_ID);
        return entity.getPersistentDataContainer().get(namespacedKeyDate, PersistentDataType.LONG);
        //TODO: Check warning
    }

    public static void unregister(Block block)
    {
        unregister(block.getLocation());
    }
    public static void unregister(Location location)
    {
        String serializedLocation = serializeLocation(location);
        NamespacedKey namespacedKeyUuid = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + UUID_ID);
        location.getChunk().getPersistentDataContainer().remove(namespacedKeyUuid);
        NamespacedKey namespacedKeyDate = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + DATE_ID);
        location.getChunk().getPersistentDataContainer().remove(namespacedKeyDate);
    }
    @Nullable
    public static UUID findPlayer(Location location)
    {
        String serializedLocation = serializeLocation(location);
        NamespacedKey namespacedKey = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + UUID_ID);
        String value = location.getChunk().getPersistentDataContainer()
                .get(namespacedKey, PersistentDataType.STRING);
        if(value == null) return null;
        return UUID.fromString(value);
    }
    @Nullable
    public static Long findDate(Block block)
    {
        return findDate(block.getLocation());
    }

    @Nullable
    public static Long findDate(Location location)
    {
        String serializedLocation = serializeLocation(location);
        NamespacedKey namespacedKey = new NamespacedKey(CreativeManager2.API.getInstance(), serializedLocation + DATE_ID);
        return location.getChunk().getPersistentDataContainer()
                .get(namespacedKey, PersistentDataType.LONG);
    }
    @Nullable
    public static UUID findPlayer(Block block)
    {
        return findPlayer(block.getLocation());
    }

    @Nullable
    public static String serializeLocation(Location location)
    {
        if(location == null) return null;
        if(location.getWorld() == null) return null;
        return location.getWorld().getUID() + "_" +
                location.getBlockX() + "_" +
                location.getBlockY() + "_" +
                location.getBlockZ();
    }
    @Nullable
    public static String properLocation(Location location)
    {
        if(location == null) return null;
        if(location.getWorld() == null) return null;
        return StringUtils.parse(location.getWorld().getName() + " [" +
                location.getBlockX() + " / " +
                location.getBlockY() + " / " +
                location.getBlockZ() + "]");
    }

}
