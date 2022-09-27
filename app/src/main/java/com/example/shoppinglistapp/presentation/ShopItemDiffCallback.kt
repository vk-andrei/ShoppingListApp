package com.example.shoppinglistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.example.shoppinglistapp.domain.ShopItem

/** Этот класс скрывает в себе всю логику работы со списком! В этом его преимущество! **/
class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopItem>() {
    override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
        return oldItem == newItem
    }
}