package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import org.bukkit.command.CommandSender;

public class MessageUtils {
    public static void sendMessage(CommandSender messageTo, String text) {
        if (!CreativeManager2.getAPI().getLang().getString(text).isEmpty())
            messageTo.sendMessage(StringUtils.parseTag(
                    StringUtils.parse(CreativeManager2.getAPI().getLang().getString(text))));
    }

    public static void sendRawMessage(CommandSender messageTo, String text) {
        if (!text.isEmpty()) messageTo.sendMessage(StringUtils.parseTag(StringUtils.parse(text)));
    }
}
