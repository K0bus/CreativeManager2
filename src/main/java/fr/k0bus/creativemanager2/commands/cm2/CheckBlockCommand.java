package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.commands.SubCommands;
import fr.k0bus.creativemanager2.CM2BlockData;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class CheckBlockCommand extends SubCommands {
    public CheckBlockCommand() {
        super("checkblock", "cm2.admin", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlockExact(5);
        if(block == null)
        {
            CM2Utils.sendRawMessage(sender, CreativeManager2.API.TAG + " &7No block targeted !");
            return;
        }

        String locationString = CM2BlockData.properLocation(block.getLocation());
        String blockString = StringUtils.proper(block.getType().name());
        String playerString;
        String dateString;

        UUID uuid = CM2BlockData.findPlayer(block);
        if(uuid == null)
        {
            CM2Utils.sendRawMessage(sender, CreativeManager2.API.TAG + " &8No log on this block !");
            return;
        }

        OfflinePlayer offlinePlayer = Bukkit.getPlayer(uuid);
        if(offlinePlayer == null)
            playerString = "UNKNOWN";
        else
            playerString = offlinePlayer.getName();
        Long dateLong = CM2BlockData.findDate(block);
        if(dateLong == null)
            dateString = "UNKNOWN";
        else
        {
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
