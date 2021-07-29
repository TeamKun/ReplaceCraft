package net.kunmc.lab.replacecraft.util

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

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

fun sendMsg(s: String) {
    Bukkit.getOnlinePlayers().forEach {
        it.sendMessage("" + s)
    }
}
