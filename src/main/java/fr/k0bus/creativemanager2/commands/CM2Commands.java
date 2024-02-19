package fr.k0bus.creativemanager2.commands;

import fr.k0bus.commands.Command;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.cm2.CheckBlockCommand;
import fr.k0bus.creativemanager2.commands.cm2.ReloadSubCommand;
import fr.k0bus.creativemanager2.commands.cm2.SettingsSubCommand;
import org.bukkit.command.CommandSender;

public class CM2Commands extends Command {
    public CM2Commands() {
        super("cm2", "cm2.admin");
        addSubCommands(new SettingsSubCommand());
        addSubCommands(new ReloadSubCommand());
        addSubCommands(new CheckBlockCommand());
        setUsage("/lagg <argument>");
        setDescription("All command added to LaggManager");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        CM2Utils.sendRawMessage(sender, CreativeManager2.API.TAG + " CreativeManager2 loaded in the server !");
    }
}
