package net.kunmc.lab.replacecraft.events

import net.kunmc.lab.replacecraft.ReplaceCraftPlugin
import org.bukkit.Bukkit
import org.bukkit.Material

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.inventory.ItemStack

class EventListener(private val plugin: ReplaceCraftPlugin): Listener {
    init {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    @EventHandler
    fun onCraft(e: CraftItemEvent) {
        if(plugin.isEnable) {
            Bukkit.getOnlinePlayers().forEach {
                for(i in 1..36) {
                    if(it.inventory.getItem(i-1) != null) {
                        val iItem = e.inventory.getItem(0)
                        val item  = it.inventory.getItem(i-1)

                        val amount = if(iItem!!.maxStackSize < item!!.amount) {
                            iItem.maxStackSize
                        }
                        else {
                            item.amount
                        }

                        it.inventory.setItem(i-1, ItemStack(iItem.type, amount))
                    }
                }
            }
        }
    }
}