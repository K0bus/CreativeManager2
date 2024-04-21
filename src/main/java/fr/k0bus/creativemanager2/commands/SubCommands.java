package fr.k0bus.creativemanager2.commands;

import org.bukkit.command.CommandSender;

import java.util.function.BiConsumer;

public abstract class SubCommands extends Command{

    BiConsumer<CommandSender,String[]> consumer;

    public SubCommands(String command, String permission, Class senderClass) {
        super(command, permission, senderClass);
    }

    public SubCommands(String command, String permission) {
        super(command, permission);
    }

    public SubCommands(String command) {
        super(command);
    }


}
