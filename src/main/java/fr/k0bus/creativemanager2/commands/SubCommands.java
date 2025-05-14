package fr.k0bus.creativemanager2.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import fr.k0bus.creativemanager2.utils.MessageUtils;
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
        return Commands.literal(getCommandString()).executes(ctx -> {
            if (!isAllowed(ctx.getSource().getSender())) {
                MessageUtils.sendMessage(ctx.getSource().getSender(), "commands.deny");
                return 1;
            }
            execute(ctx.getSource().getSender());
            return 1;
        });
    }
}
