package net.kunmc.lab.replacecraft.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.translation.GlobalTranslator
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun log(sender: CommandSender, s: String) {
    sender.sendMessage(s)
}

fun info(sender: CommandSender, s: String) {
    sender.sendMessage("" + ChatColor.GREEN + s)
}

fun warn(sender: CommandSender, s: String) {
    sender.sendMessage("" + ChatColor.GOLD + "WARN: " + s)
}

fun error(sender: CommandSender, s: String) {
    sender.sendMessage("" + ChatColor.RED + "ERROR: " + s)
}

/**
 * @author bun133
 */
fun translate(p: Player,material: Material): Component {
    val e = Component.translatable(material.translationKey)
    val translator = GlobalTranslator.renderer()
    return translator.render(e, p.locale())
}
