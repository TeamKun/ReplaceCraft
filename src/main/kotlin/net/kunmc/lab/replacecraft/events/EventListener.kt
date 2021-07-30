package net.kunmc.lab.replacecraft.events

import net.kunmc.lab.replacecraft.ReplaceCraftPlugin
import net.kunmc.lab.replacecraft.util.*

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.Style
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.InventoryType
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
                it.sendMessage(Component.text("${e.whoClicked.name}が").append(translate(it, resultItem!!.type)).append(Component.text("をクラフトした！")).style(
                    Style.style(NamedTextColor.GOLD)))

                if(it.gameMode != GameMode.CREATIVE && it.gameMode != GameMode.SPECTATOR) {
                    // クラフトインベントリ内のアイテム置き換え
                    // クラフトしたプレイヤー以外
                    val type = it.openInventory.topInventory.type
                    if(type == InventoryType.WORKBENCH || type == InventoryType.CRAFTING) {
                        if(e.whoClicked.uniqueId != it.uniqueId) {
                            val cInv = it.openInventory.topInventory

                            for(i in 1 until cInv.size) {
                                val item = cInv.getItem(i)

                                if(item != null && 0 < item.amount) {
                                    cInv.setItem(i, ItemStack(resultItem.type, checkItemAmount(resultItem, item)))
                                }
                                else {
                                    cInv.setItem(i, ItemStack(Material.AIR))
                                }
                            }
                        }
                        // クラフトをしたプレイヤー
                        else {
                            val cInv = it.openInventory.topInventory
                            var amount: Int = e.recipe.result.amount

                            // シフトクリックでアイテムをクラフトした場合
                            if(e.isShiftClick) {
                                var max: Int = e.inventory.maxStackSize
                                val matrix = e.inventory.matrix

                                // クラフトしたアイテムの個数を計算
                                for(i in matrix) {
                                    if(i != null) {
                                        if(i.type == Material.AIR) {
                                            continue
                                        }
                                        val tmp = i.amount
                                        if(tmp in 1 until max) {
                                            max = tmp
                                        }
                                    }
                                }
                                amount *= max

                                // 空きがない場合クラフトできないのでイベントをキャンセル
                                val space: Int = getSpaceSize(resultItem, it)
                                if(space == 0) {
                                    e.isCancelled = true
                                    return
                                }

                                // 空きスペースよりもクラフトアイテム数が多かった場合
                                if(space < amount) {
                                    var tmp = space % resultItem.amount
                                    it.inventory.addItem(ItemStack(resultItem.type, space - tmp))

                                    tmp = space / resultItem.amount
                                    for(i in 1 until cInv.size) {
                                        val item = cInv.getItem(i)

                                        if(item != null && 0 < item.amount - tmp) {
                                            cInv.setItem(i, ItemStack(resultItem.type, checkItemAmount(resultItem, item.amount - tmp)))
                                        }
                                        else {
                                            cInv.setItem(i, ItemStack(Material.AIR))
                                        }
                                    }
                                }
                                else {
                                    it.inventory.addItem(ItemStack(resultItem.type, amount))
                                    for(i in 1 until cInv.size) {
                                        val item = cInv.getItem(i)

                                        if(item != null && 0 < item.amount - (amount / resultItem.amount)) {
                                            cInv.setItem(i, ItemStack(resultItem.type, checkItemAmount(resultItem, item.amount - (amount / resultItem.amount))))
                                        }
                                        else {
                                            cInv.setItem(i, ItemStack(Material.AIR))
                                        }
                                    }
                                }
                            }
                            else {
                                for(i in 1 until cInv.size) {
                                    val item = cInv.getItem(i)

                                    if(item != null && 0 < item.amount - 1) {
                                        cInv.setItem(i, ItemStack(resultItem.type, checkItemAmount(resultItem, item.amount - 1)))
                                    }
                                    else {
                                        cInv.setItem(i, ItemStack(Material.AIR))
                                    }
                                }
                                it.openInventory.cursor = resultItem
                            }
                        }
                    }

                    // カーソルのアイテム置き換え
                    val pInv = it.openInventory
                    if(pInv.cursor != null) {
                        val amount = checkItemAmount(resultItem, pInv.cursor)
                        pInv.cursor = ItemStack(resultItem.type, amount)
                    }

                    // オフハンドのアイテム置き換え
                    if(it.inventory.itemInOffHand.type != Material.AIR) {
                        val item   = it.inventory.itemInOffHand
                        val amount = checkItemAmount(resultItem, item)

                        it.inventory.setItemInOffHand(ItemStack(resultItem.type, amount))
                    }

                    // インベントリ内のアイテム置き換え
                    for(i in 1..36) {
                        if(it.inventory.getItem(i-1) != null) {
                            val item   = it.inventory.getItem(i-1)
                            val amount = checkItemAmount(resultItem, item)

                            it.inventory.setItem(i-1, ItemStack(resultItem.type, amount))
                        }
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

    // クラフトしたアイテムがインベントリに入る最大量の計算
    private fun getSpaceSize(item: ItemStack?, p: Player): Int {
        var size = 0
        val inv = p.inventory

        for(i in 1..36) {
            val iItem: ItemStack? = inv.getItem(i-1)
            if(iItem != null && item!!.type == iItem.type) {
                size += iItem.maxStackSize - iItem.amount
            }
            else if(iItem == null) {
                size += item!!.maxStackSize
            }
        }
        return size
    }
}
