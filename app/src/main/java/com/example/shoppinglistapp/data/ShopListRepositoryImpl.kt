package com.example.shoppinglistapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglistapp.domain.ShopItem
import com.example.shoppinglistapp.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository {

    private val shopList = mutableListOf<ShopItem>()
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private var autoIncrementId = 0

    /** FOR TEST **/
    init {
        for (i in 0..10) {
            val item = ShopItem("Name$i", i, true)
            addShopItem(item)
        }
    }

    /*****/

    override fun addShopItem(shopItem: ShopItem) {
        if (shopItem.id == ShopItem.UNDEFINED_ID) {
            shopItem.id = autoIncrementId
            autoIncrementId++
        }
        shopList.add(shopItem)
        updateList()
    }

    override fun removeShopItem(shopItem: ShopItem) {
        shopList.remove(shopItem)
        updateList()
    }

    override fun editShopItem(shopItem: ShopItem) {
        val oldShopItem = getShopItem(shopItem.id)
        shopList.remove(oldShopItem)
        addShopItem(shopItem)
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return shopList.find {
            it.id == shopItemId
        } ?: throw RuntimeException("Element with id $shopItemId not found")
    }

    /*override fun getShopList(): List<ShopItem> {
        return shopList.toList()   // возвращаем копию нашего листа покупок
    }*/

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData   // возвращаем копию нашего листа покупок
    }

    private fun updateList() {
        shopListLiveData.value = shopList.toList()
    }
}