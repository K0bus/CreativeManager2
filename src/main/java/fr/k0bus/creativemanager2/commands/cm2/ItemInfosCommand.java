package fr.k0bus.creativemanager2.commands.cm2;

import de.tr7zw.nbtapi.NBT;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.SubCommands;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import java.util.Map;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemInfosCommand extends SubCommands {
    public ItemInfosCommand() {
        super("iteminfos", "cm2.admin.iteminfos", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        execute(sender);
    }

    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType().equals(Material.AIR)) {
            CM2Utils.sendMessage(player, "commands.iteminfos.no-items");
            return;
        }
        StringBuilder tags = new StringBuilder();
        for (Map.Entry<String, Set<Material>> entry :
                CreativeManager2.getAPI().getTagMap().entrySet()) {
            if (entry.getValue().contains(itemStack.getType())) {
                if (!tags.toString().isEmpty()) tags.append(", ");
                tags.append("§r#").append(entry.getKey()).append("§6");
            }
        }
        StringBuilder nbtKey = new StringBuilder();
        NBT.get(itemStack, nbt -> {
            for (String k : nbt.getKeys()) {
                if (!nbtKey.toString().isEmpty()) nbtKey.append(", ");
                nbtKey.append("§r").append(k).append("§6");
            }
        });
        CM2Utils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        CM2Utils.sendRawMessage(sender, " &6You've requested items informations below");
        CM2Utils.sendRawMessage(sender, " &8- &3Name : &7" + itemStack.getType().name());
        CM2Utils.sendRawMessage(sender, " &8- &3Tags : &6[&7" + tags + "&r&6]");
        CM2Utils.sendRawMessage(sender, " &8- &3NBT Keys : &6[&7" + nbtKey + "&r&6]");
        CM2Utils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
