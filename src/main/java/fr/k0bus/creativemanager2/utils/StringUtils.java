package fr.k0bus.creativemanager2.utils;

import fr.k0bus.creativemanager2.CreativeManager2;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class StringUtils {

    public static String parse(String s) {
        return Utils.placeholderApiParse(parseString(s));
    }

    public static String parseString(String s) {
        TextComponent builder = LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        return LegacyComponentSerializer.legacySection().serialize(builder);
    }
    public static TextComponent parseComponent(String s) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(s);
    }

    public static String proper(String str) {
        String modifiedStr = str.replace("_", " ");
        String[] words = modifiedStr.split(" ");
        StringBuilder finalString = new StringBuilder();

        for (String word : words) {
            String capitalizedWord = word.substring(0, 1).toUpperCase(Locale.getDefault())
                    + word.substring(1).toLowerCase(Locale.getDefault());
            if (!finalString.isEmpty()) {
                finalString.append(' ');
            }
            finalString.append(capitalizedWord);
        }

        return finalString.toString();
    }

    public static String replacePlaceholders(String template, Map<String, String> values) {
        Pattern pattern = Pattern.compile("\\{(\\w+)}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            String replacement = values.getOrDefault(key, matcher.group(0));
            matcher.appendReplacement(sb, replacement);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static List<String> listToLowerCase(List<String> list) {
        return list.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public static String parseTag(String string) {
        return string.replace("{TAG}", CreativeManager2.getAPI().getTag());
    }
}
