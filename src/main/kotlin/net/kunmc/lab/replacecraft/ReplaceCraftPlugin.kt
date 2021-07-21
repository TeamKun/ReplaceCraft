package net.kunmc.lab.replacecraft

import net.kunmc.lab.replacecraft.commands.CommandListener
import net.kunmc.lab.replacecraft.commands.TabCompleter
import net.kunmc.lab.replacecraft.events.*
import org.bukkit.plugin.java.JavaPlugin

class ReplaceCraftPlugin: JavaPlugin() {
    var isEnable: Boolean = false

    override fun onEnable() {
        CommandListener(this)
        EventListener(this)
        TabCompleter(this)
    }
}