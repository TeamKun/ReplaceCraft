package net.kunmc.lab.replacecraft.commands

import net.kunmc.lab.replacecraft.ReplaceCraftPlugin
import net.kunmc.lab.replacecraft.util.*

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class CommandListener(private val plugin: ReplaceCraftPlugin): CommandExecutor {
    init {
        plugin.getCommand("replace")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        if(args.isEmpty()) {
            error(sender, "引数の数が不正です")
            return true
        }

        when(args[0]) {
            "on"  -> on(sender)
            "off" -> off(sender)
            else  -> error(sender, "不正な引数が入力されました")
        }

        return true
    }

    private fun on(sender: CommandSender) {
        if(plugin.isEnable) {
            warn(sender, "もうかいししてるよ～")
            return
        }
        plugin.isEnable = true
        info(sender, "ReplaceCraftが有効になりました")
    }

    private fun off(sender: CommandSender) {
        if(!plugin.isEnable) {
            warn(sender, "いまむこうだよ")
            return
        }
        plugin.isEnable = false
        info(sender, "ReplaceCraftが無効になりました")
    }
}