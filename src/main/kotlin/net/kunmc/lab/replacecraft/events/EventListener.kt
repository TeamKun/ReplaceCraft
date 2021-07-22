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

            Bukkit.getOnlinePlayers().forEach {
                if(it.gameMode != GameMode.CREATIVE && it.gameMode != GameMode.SPECTATOR) {
                    // カーソルのアイテム置き換え
                    val pInv = it.openInventory
                    if(pInv.cursor != null) {
                        val amount = checkItemAmount(resultItem, pInv.cursor)
                        pInv.cursor = ItemStack(resultItem!!.type, amount)
                    }

                    // オフハンドのアイテム置き換え
                    if(it.inventory.itemInOffHand.type != Material.AIR) {
                        val item   = it.inventory.itemInOffHand
                        val amount = checkItemAmount(resultItem, item)

                        it.inventory.setItemInOffHand(ItemStack(resultItem!!.type, amount))
                    }

                    // インベントリ内のアイテム置き換え
                    for(i in 1..36) {
                        if(it.inventory.getItem(i-1) != null) {
                            val item   = it.inventory.getItem(i-1)
                            val amount = checkItemAmount(resultItem, item)

                            it.inventory.setItem(i-1, ItemStack(resultItem!!.type, amount))
                        }
                    }

                    // クラフトインベントリ内のアイテム置き換え
                    // クラフトしたプレイヤー以外
                    if(e.whoClicked.uniqueId != it.uniqueId) {
                        val cInv = it.openInventory.topInventory
                        for(i in 1 until cInv.size) {
                            val item = cInv.getItem(i)

                            if(item != null && 0 < item.amount) {
                                cInv.setItem(i, ItemStack(resultItem!!.type, checkItemAmount(resultItem, item)))
                            }
                            else {
                                cInv.setItem(i, ItemStack(Material.AIR))
                            }
                        }
                    }
                    // クラフトをしたプレイヤー
                    else {
                        val cInv = it.openInventory.topInventory
                        for(i in 1 until cInv.size) {
                            val item = cInv.getItem(i)

                            if(item != null && 0 < item.amount - 1) {
                                cInv.setItem(i, ItemStack(resultItem!!.type, checkItemAmount(resultItem, item.amount - 1)))
                            }
                            else {
                                cInv.setItem(i, ItemStack(Material.AIR))
                            }
                        }
                        it.openInventory.cursor = resultItem
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
    private fun checkItemAmount(iItem: ItemStack?, item: Int): Int {
        return if(iItem!!.maxStackSize < item) {
            iItem.maxStackSize
        }
        else {
            item
        }
    }
}