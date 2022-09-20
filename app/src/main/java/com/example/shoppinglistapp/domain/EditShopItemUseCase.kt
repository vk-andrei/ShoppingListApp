package com.example.shoppinglistapp.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun editShopItem(shopItem: ShopItem): ShopItem {
        return shopListRepository.editShopItem(shopItem)
    }
}