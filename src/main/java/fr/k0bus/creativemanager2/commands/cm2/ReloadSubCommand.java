package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.commands.SubCommands;
import fr.k0bus.creativemanager2.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommands {
    public ReloadSubCommand() {
        super("reload", "cm2.admin.reload");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        CreativeManager2.API.reloadSettings();
        CM2Utils.sendRawMessage(sender, CreativeManager2.API.TAG + " &2Settings & Language reloaded !");
    }
}
