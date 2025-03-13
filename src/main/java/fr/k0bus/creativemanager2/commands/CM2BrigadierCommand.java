package fr.k0bus.creativemanager2.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.k0bus.creativemanager2.CreativeManager2;
import fr.k0bus.creativemanager2.commands.cm2.CheckBlockCommand;
import fr.k0bus.creativemanager2.commands.cm2.ItemInfosCommand;
import fr.k0bus.creativemanager2.commands.cm2.ReloadSubCommand;
import fr.k0bus.creativemanager2.commands.cm2.SettingsSubCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("UnstableApiUsage")
public class CM2BrigadierCommand {

    private final CreativeManager2 plugin;

    public CM2BrigadierCommand(CreativeManager2 instance)
    {
        plugin = instance;
    }

    public void build()
    {
        LifecycleEventManager<Plugin> manager = plugin.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            LiteralCommandNode<CommandSourceStack> buildCommand = Commands.literal("cm2")
                    .executes(ctx -> {
                        CM2BukkitCommands.sendMainMessage(ctx.getSource().getSender());
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(
                            new ReloadSubCommand().getCommandNode()
                    ).then(
                            new CheckBlockCommand().getCommandNode()
                    ).then(
                            new SettingsSubCommand().getCommandNode()
                    ).then(
                            new ItemInfosCommand().getCommandNode()
                    ).build();
            commands.registrar().register(buildCommand);
        });
    }
}
