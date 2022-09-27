package com.example.shoppinglistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglistapp.domain.ShopItem
/** Этот класс не нужен. Использовался раньше. (он работает в главном потоке)
 * Сейчас более умный - ShopItemDiffCallback (это сначала делает изм-я, потом обновляет список)
 * **/
class ShopListDiffCallback(
    private val oldList: List<ShopItem>,
    private val newList: List<ShopItem>
) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}