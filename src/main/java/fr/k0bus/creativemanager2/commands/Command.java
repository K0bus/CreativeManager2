package fr.k0bus.creativemanager2.commands;

import fr.k0bus.creativemanager2.utils.CM2Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/** Main command class. */
@SuppressWarnings({"rawtypes", "unused"})
public class Command implements CommandExecutor, TabCompleter {
    private final HashMap<String, SubCommands> subCommands = new HashMap<>();
    private final String permission;
    private final String command;

    private String description = "Default command description";
    private String usage = "/<cmd>";

    private final Class senderClass;

    private final HashMap<Integer, List<String>> completer = new HashMap<>();

    public Command(String command, String permission, Class senderClass) {
        this.command = command;
        this.permission = permission;
        this.senderClass = senderClass;
    }

    public Command(String command, String permission) {
        this(command, permission, CommandSender.class);
    }

    public Command(String command) {
        this(command, null, CommandSender.class);
    }

    public void addSubCommands(SubCommands subCommands) {
        this.subCommands.put(subCommands.getCommand(), subCommands);
    }

    public HashMap<String, SubCommands> getSubCommands() {
        return subCommands;
    }

    public String getCommand() {
        return command;
    }

    public String getPermission() {
        return permission;
    }

    public Class getSenderClass() {
        return senderClass;
    }

    public boolean isAllowed(CommandSender sender) {
        if (!senderClass.isInstance(sender)) return false;
        return permission == null || sender.hasPermission(permission);
    }

    public void setCompleter(int args, List<String> list) {
        completer.put(args, list);
    }

    public void run(CommandSender sender, String... args) {}

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender,
            @NotNull org.bukkit.command.Command command,
            @NotNull String label,
            @NotNull String[] args) {
        if (!isAllowed(sender)) {
            sender.sendMessage("Not allowed");
            return true;
        }
        if (!subCommands.isEmpty()) {
            if (args.length > 0) {
                if (subCommands.containsKey(args[0])) {
                    subCommands
                            .get(args[0])
                            .onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    sender.sendMessage("Bad commands");
                }
                return true;
            } else {
                sender.sendMessage("Bad commands");
            }
        }
        run(sender, args);
        return true;
    }

    @NotNull
    @Override
    public List<String> onTabComplete(
            @NotNull CommandSender sender,
            @NotNull org.bukkit.command.Command command,
            @NotNull String label,
            @NotNull String[] args) {
        List<String> complete = new ArrayList<>();
        if (!subCommands.isEmpty()) {
            if (args.length == 1) {
                for (Map.Entry<String, SubCommands> e : subCommands.entrySet()) {
                    if (e.getKey() != null
                            && e.getKey().startsWith(args[0].toLowerCase())
                            && e.getValue().isAllowed(sender))
                        complete.add(e.getValue().getCommand());
                }
            } else if (args.length == 0) {
                for (SubCommands sub : subCommands.values()) {
                    if (sub.isAllowed(sender)) complete.add(sub.getCommand());
                }
            } else {
                if (subCommands.containsKey(args[0])) {
                    SubCommands sc = subCommands.get(args[0]);
                    if (sc != null) {
                        complete.addAll(
                                sc.onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length)));
                    }
                }
            }
        }
        if (args.length == 0) {
            if (completer.containsKey(args.length)) complete.addAll(completer.get(args.length));
        } else {
            if (completer.containsKey(args.length - 1)) complete.addAll(completer.get(args.length - 1));
        }
        return complete;
    }

    public org.bukkit.command.Command getRawCommand() {
        return new org.bukkit.command.Command(this.command, description, usage, new ArrayList<>()) {
            @Override
            public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                return onCommand(commandSender, this, s, strings);
            }
        };
    }

    public void register(JavaPlugin plugin) {

        if (CM2Utils.isPaper()) {
            PluginCommand cmd = plugin.getCommand(getCommand());
            if (cmd != null) {
                cmd.setExecutor(this);
                cmd.setTabCompleter(this);
            }
        } else {
            PluginCommand cmd = plugin.getCommand(getCommand());
            if (cmd != null) {
                cmd.setExecutor(this);
                cmd.setTabCompleter(this);
            }
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
