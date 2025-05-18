package fr.k0bus.creativemanager2.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"UnstableApiUsage", "rawtypes"})
public abstract class SubCommands extends Command {

    public SubCommands(String command, String permission, Class senderClass) {
        super(command, permission, senderClass);
    }

    public SubCommands(String command, String permission) {
        super(command, permission);
    }

    public abstract void execute(CommandSender sender);

    public LiteralArgumentBuilder<CommandSourceStack> getCommandNode() {
        return Commands.literal(getCommandString())
                .requires(source -> isAllowed(source.getSender()))
                .executes(ctx -> {
                    execute(ctx.getSource().getSender());
                    return 1;
                });
    }
}
