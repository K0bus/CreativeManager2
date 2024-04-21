package fr.k0bus.creativemanager2.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String parse(String s)
    {
        return Utils.PAPIParse(translateColor(s));
    }

    public static String translateColor(String s) {

        Pattern pattern = Pattern.compile("#[a-fA-F\u0000-9]{6}");

        for(Matcher matcher = pattern.matcher(s); matcher.find(); matcher = pattern.matcher(s)) {
            String hexCode = s.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();

            for (char c : ch) {
                builder.append("&").append(c);
            }

            s = s.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', s);
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
