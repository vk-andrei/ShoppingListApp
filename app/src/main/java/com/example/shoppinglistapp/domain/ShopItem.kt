package com.example.shoppinglistapp.domain

data class ShopItem(
    val name: String,
    val qty: Int,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}
