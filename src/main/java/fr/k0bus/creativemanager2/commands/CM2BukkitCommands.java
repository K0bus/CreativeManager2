package fr.k0bus.creativemanager2.commands;

import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.cm2.*;
import fr.k0bus.creativemanager2.utils.MessageUtils;
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
        super.addSubCommands(new ItemDebugCommand());
        super.setUsage("/cm2 <argument>");
        super.setDescription("All command added to CreativeManager2");
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sendMainMessage(sender);
    }

    private static void sendMainMessage(CommandSender sender) {
        MessageUtils.sendRawMessage(
                sender, CreativeManager2.getAPI().getTag() + " CreativeManager2 loaded in the server !");
    }
}
