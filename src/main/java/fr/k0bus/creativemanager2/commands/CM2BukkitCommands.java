package fr.k0bus.creativemanager2.commands;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.cm2.CheckBlockCommand;
import fr.k0bus.creativemanager2.commands.cm2.ItemInfosCommand;
import fr.k0bus.creativemanager2.commands.cm2.ReloadSubCommand;
import fr.k0bus.creativemanager2.commands.cm2.SettingsSubCommand;
import fr.k0bus.creativemanager2.utils.CM2Utils;
import org.bukkit.command.CommandSender;

public class CM2BukkitCommands extends Command {
    public CM2BukkitCommands() {
        super("cm2", "cm2.admin");
        this.init();
    }

    private void init() {
        super.addSubCommands(new SettingsSubCommand());
        super.addSubCommands(new ReloadSubCommand());
        super.addSubCommands(new CheckBlockCommand());
        super.addSubCommands(new ItemInfosCommand());
        super.setUsage("/cm2 <argument>");
        super.setDescription("All command added to CreativeManager2");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sendMainMessage(sender);
    }

    private static void sendMainMessage(CommandSender sender) {
        CM2Utils.sendRawMessage(sender, CreativeManager2.getAPI().getTag() + " CreativeManager2 loaded in the server !");
    }
}
