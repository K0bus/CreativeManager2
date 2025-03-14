package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.SubCommands;
import fr.k0bus.creativemanager2.utils.CM2Data;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.utils.StringUtils;
import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckBlockCommand extends SubCommands {
    public CheckBlockCommand() {
        super("checkblock", "cm2.admin.checkblock", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        execute(sender);
    }

    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(5);
        if (block == null) {
            CM2Utils.sendRawMessage(sender, CreativeManager2.api.tag + " &7No block targeted !");
            return;
        }

        String locationString = CM2Data.properLocation(block.getLocation());
        String blockString = StringUtils.proper(block.getType().name());
        String playerString;
        String dateString;

        UUID uuid = CM2Data.findPlayer(block);
        if (uuid == null) {
            CM2Utils.sendRawMessage(sender, CreativeManager2.api.tag + " &8No log on this block !");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getPlayer(uuid);
        if (offlinePlayer == null) playerString = "UNKNOWN";
        else playerString = offlinePlayer.getName();
        long dateLong = CM2Data.findDate(block);
        if (dateLong == -1) dateString = "UNKNOWN";
        else {
            Date date = new Date(dateLong);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            dateString = dateFormat.format(date);
        }
        CM2Utils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
        CM2Utils.sendRawMessage(sender, " &8- &3Location : &7" + locationString);
        CM2Utils.sendRawMessage(sender, " &8- &3Block : &7" + blockString);
        CM2Utils.sendRawMessage(sender, " &8- &3Player : &6" + playerString);
        CM2Utils.sendRawMessage(sender, " &8- &3Date : &6" + dateString);
        CM2Utils.sendRawMessage(sender, "&8▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
    }
}
