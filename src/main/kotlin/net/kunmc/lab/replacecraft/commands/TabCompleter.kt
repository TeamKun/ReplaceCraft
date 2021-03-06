package net.kunmc.lab.replacecraft.commands

import net.kunmc.lab.replacecraft.ReplaceCraftPlugin
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class TabCompleter(plugin: ReplaceCraftPlugin): TabCompleter {
    init {
        plugin.getCommand("replace")!!.setTabCompleter(this)
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): MutableList<String>? {
        var result: MutableList<String> = mutableListOf()
        if(args.size == 1) {
            result.addAll(listOf("on", "off"))
            result = result.filter {
                it.startsWith(args[0])
            }.toMutableList()
        }
        else {
            result.clear()
        }
        return result
    }
}