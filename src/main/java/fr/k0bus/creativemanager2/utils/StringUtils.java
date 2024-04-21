package fr.k0bus.creativemanager2.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#(\\w{5}[0-9a-f])");

    public static String parse(String s)
    {
        return Utils.PAPIParse(translateColor(s));
    }

    public static String translateColor(String s) {

        if (VersionUtils.getMCVersion() < 16) {
            return ChatColor.translateAlternateColorCodes('&', s);
        }

        Matcher matcher = HEX_PATTERN.matcher(s);
        StringBuffer buffer = new StringBuffer();

        while(matcher.find()) {
            matcher.appendReplacement(buffer, ChatColor.of("#" + matcher.group(1)).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
    }

    public static String proper(String str)
    {
        str = str.replace("_", " ");
        String[] strings = str.split(" ");
        StringBuilder finalString = new StringBuilder();
        for (String s:strings) {
            s = s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase();
            if(!finalString.toString().equals("")) finalString.append(" ");
            finalString.append(s);
        }
        return finalString.toString();
    }
}
