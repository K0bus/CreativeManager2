package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.creativemanager2.commands.SubCommands;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.command.CommandSender;

public class ReloadSubCommand extends SubCommands {
    public ReloadSubCommand() {
        super("reload", "cm2.admin.reload");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        execute(sender);
    }

    public void execute(CommandSender sender)
    {
        CreativeManager2.api.reloadSettings();
        CM2Utils.sendRawMessage(sender, CreativeManager2.api.tag + " &2Settings & Language reloaded !");
    }
}
