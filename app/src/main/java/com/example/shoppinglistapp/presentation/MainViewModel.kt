package com.example.shoppinglistapp.presentation

import androidx.lifecycle.ViewModel
import com.example.shoppinglistapp.data.ShopListRepositoryImpl // NOT GOOD!!!!!!!!!!!
import com.example.shoppinglistapp.domain.*

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)
    private val removeShopItemUseCase = RemoveShopItemUseCase(repository)

    //val shopList = MutableLiveData<List<ShopItem>>()
    val shopList = getShopListUseCase.getShopList()

    /*fun getShopList() {
        val list = getShopListUseCase.getShopList()
        shopList.value = list
    }*/

    fun removeShopItem(shopItem: ShopItem) {
        removeShopItemUseCase.removeShopItem(shopItem)
        // getShopList() // for UPDATE list
    }

    fun changeEnableState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newItem)
        //getShopList()
    }


}