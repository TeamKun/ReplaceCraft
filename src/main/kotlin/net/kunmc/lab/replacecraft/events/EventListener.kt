package net.kunmc.lab.replacecraft.events

import net.kunmc.lab.replacecraft.ReplaceCraftPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
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
            val resultItem = e.inventory.getItem(0)

            // 作業台に配置されているアイテムの置き換え
            for(i in 0..8) {
                val item = e.inventory.matrix.get(i)

                if(item != null && 0 < item.amount - 1) {
                    val amount = item.amount - 1
                    e.whoClicked.sendMessage("" + item)
                    e.inventory.setItem(i+1, ItemStack(resultItem!!.type, checkItemAmount(resultItem, amount)))
                }
                else {
                    e.inventory.setItem(i+1, ItemStack(Material.AIR))
                }
            }

            Bukkit.getOnlinePlayers().forEach {
                if(it.gameMode != GameMode.CREATIVE && it.gameMode != GameMode.SPECTATOR) {
                    // オフハンドのアイテム置き換え
                    if(it.inventory.itemInOffHand.type != Material.AIR) {
                        val iItem  = e.inventory.getItem(0)
                        val item   = it.inventory.itemInOffHand
                        val amount = checkItemAmount(iItem, item)

                        it.inventory.setItemInOffHand(ItemStack(iItem!!.type, amount))
                    }

                    // インベントリ内のアイテム置き換え
                    for(i in 1..36) {
                        if(it.inventory.getItem(i-1) != null) {
                            val item   = it.inventory.getItem(i-1)
                            val amount = checkItemAmount(resultItem, item)

                            it.inventory.setItem(i-1, ItemStack(resultItem!!.type, amount))
                        }
                    }

                    // カーソルのアイテム置き換え
                    val pInv = it.openInventory
                    if(pInv.cursor != null) {
                        val amount = checkItemAmount(resultItem, pInv.cursor)
                        pInv.cursor = ItemStack(resultItem!!.type, amount)
                    }
                }
            }
        }
    }

    private fun checkItemAmount(iItem: ItemStack?, item: ItemStack?): Int {
        return if(iItem!!.maxStackSize < item!!.amount) {
            iItem.maxStackSize
        }
        else {
            item.amount
        }
    }
    private fun checkItemAmount(iItem: ItemStack?, amount: Int): Int {
        return if(iItem!!.maxStackSize < amount) {
            iItem.maxStackSize
        }
        else {
            amount
        }
    }
}