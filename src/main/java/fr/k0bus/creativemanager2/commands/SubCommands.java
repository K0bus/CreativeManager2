package fr.k0bus.creativemanager2.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.function.BiConsumer;
import org.bukkit.command.CommandSender;

@SuppressWarnings({"UnstableApiUsage", "rawtypes", "unused"})
public abstract class SubCommands extends Command {

    BiConsumer<CommandSender, String[]> consumer;

    public SubCommands(String command, String permission, Class senderClass) {
        super(command, permission, senderClass);
    }

    public SubCommands(String command, String permission) {
        super(command, permission);
    }

    public SubCommands(String command) {
        super(command);
    }

    public abstract void execute(CommandSender sender);

    public LiteralArgumentBuilder<CommandSourceStack> getCommandNode() {
        return Commands.literal(getCommand()).executes(ctx -> {
            execute(ctx.getSource().getSender());
            return 1;
        });
    }
}
