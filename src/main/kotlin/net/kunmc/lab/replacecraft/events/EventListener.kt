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

//            var amount: Int = e.recipe.result.amount
//            if(e.isShiftClick) {
//                var max: Int = e.inventory.maxStackSize
//                val matrix = e.inventory.matrix
//
//                for(i in matrix) {
//                    if(i != null) {
//                        if(i.type == Material.AIR) {
//                            continue
//                        }
//                        val tmp = i.amount
//                        if(tmp in 1 until max) {
//                            max = tmp
//                        }
//                    }
//                }
//                amount *= max
//            }

            Bukkit.getOnlinePlayers().forEach {
                for(i in 1..36) {
                    if(it.inventory.getItem(i-1) != null) {
                        it.inventory.setItem(i-1, e.inventory.getItem(0))
                    }
                }
            }
        }
    }
}