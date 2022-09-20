package com.example.shoppinglistapp.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun getShopItemCase(shopItemId: Int): ShopItem {
        return shopListRepository.getShopItemCase(shopItemId)
    }

}