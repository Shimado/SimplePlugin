package me.shimado.simpleplugin

import org.bukkit.ChatColor
import java.util.regex.Pattern
import java.util.stream.Collectors

class SText {

    private val pattern = Pattern.compile("#[a-fA-F0-9]{6}")

    /**
     * ОКРАШИВАЕТ ТЕКСТ
     */
    fun getColor(text: String): String {
        var text = text;
        var matcher = pattern.matcher(text)
        while (matcher.find()) {
            val color = text.substring(matcher.start(), matcher.end())
            text = text.replace(color, "${net.md_5.bungee.api.ChatColor.of(color)} ")
            matcher = pattern.matcher(text)
        }
        return ChatColor.translateAlternateColorCodes('&', text)
    }

    /**
     * ОКРАШИВАЕТ ОПИСАНИЕ ПРЕДМЕТА
     */
    fun getColorList(text: List<String>): List<String> {
        return text.stream().map { l: String -> getColor(l) }.collect(Collectors.toList())
    }

}