package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.commands.SubCommands;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsSubCommand extends SubCommands {
    public SettingsSubCommand() {
        super("settings", "cm2.admin", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        CM2Utils.sendRawMessage(sender, CreativeManager2.API.TAG + " &6Opening settings GUI...");
    }
}
