package fr.k0bus.creativemanager2.commands.cm2;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.SubCommands;
import fr.k0bus.creativemanager2.gui.SettingGui;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SettingsSubCommand extends SubCommands {
    public SettingsSubCommand() {
        super("settings", "cm2.admin.settings", Player.class);
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        execute(sender);
    }

    @Override
    public void execute(CommandSender sender) {
        if (sender instanceof Player p)
            new SettingGui(CreativeManager2.api.getInstance()).init().open(p);
    }
}
