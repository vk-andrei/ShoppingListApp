package com.example.shoppinglistapp.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ShopListRepository {

    fun addShopItem(shopItem: ShopItem)

    fun removeShopItem(shopItem: ShopItem)

    fun editShopItem(shopItem: ShopItem)

    fun getShopItem(shopItemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>

}