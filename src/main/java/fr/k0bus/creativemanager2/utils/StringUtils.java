package fr.k0bus.creativemanager2.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class StringUtils {

    public static String parse(String s)
    {
        return Utils.placeholderApiParse(translateColor(s));
    }

    public static String translateColor(String s) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        String result = s; // Utilisation d'une variable locale

        for (Matcher matcher = pattern.matcher(result); matcher.find(); matcher = pattern.matcher(result)) {
            String hexCode = result.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');
            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();

            for (char c : ch) {
                builder.append('&').append(c);
            }

            result = result.replace(hexCode, builder.toString());
        }

        return ChatColor.translateAlternateColorCodes('&', result);
    }

    public static String proper(String str) {
        String modifiedStr = str.replace("_", " ");
        String[] words = modifiedStr.split(" ");
        StringBuilder finalString = new StringBuilder();

        for (String word : words) {
            String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
            if (!finalString.isEmpty()) {
                finalString.append(' ');
            }
            finalString.append(capitalizedWord);
        }

        return finalString.toString();
    }
}
