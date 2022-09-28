package com.example.shoppinglistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglistapp.data.ShopListRepositoryImpl
import com.example.shoppinglistapp.domain.AddShopItemUseCase
import com.example.shoppinglistapp.domain.EditShopItemUseCase
import com.example.shoppinglistapp.domain.GetShopItemUseCase
import com.example.shoppinglistapp.domain.ShopItem

class ShopItemViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopItemUseCase = GetShopItemUseCase(repository)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputQty = MutableLiveData<Boolean>()
    val errorInputQty: LiveData<Boolean>
        get() = _errorInputQty

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    //т.к. в это методе всегда ТРУ то можно установть UNIT (вместо Boolean).
    // важен сам факт попадания сюда
    private val _shouldCloseDisplay = MutableLiveData<Unit>()
    val shouldCloseDisplay: LiveData<Unit>
        get() = _shouldCloseDisplay

    fun getShopItem(shopItemId: Int) {
        val item = getShopItemUseCase.getShopItem(shopItemId)
        _shopItem.value = item
    }

    fun addShopItem(inputName: String?, inputQty: String?) {
        val name = parseName(inputName)
        val qty = parseQty(inputQty)
        val fieldsValid = validateInput(name, qty)
        if (fieldsValid) {
            val shopItem = ShopItem(name, qty, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()
        }
    }

    fun editShopItem(inputName: String?, inputQty: String?) {
        val name = parseName(inputName)
        val qty = parseQty(inputQty)
        val fieldsValid = validateInput(name, qty)
        if (fieldsValid) {
            // берем айтем из LiveData. Если он там есть и не равен NULL:
            // делаем его копии у нее
            // меняем имя и кол-во (а id и enabled остаются какимим и были)
            _shopItem.value?.let {
                val item = it.copy(name = name, qty = qty)
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseQty(inputQty: String?): Double {
        return try {
            inputQty?.trim()?.toDouble() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    private fun validateInput(name: String, qty: Double): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (qty < 0) {
            _errorInputQty.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputQty() {
        _errorInputName.value = false
    }

    private fun finishWork() {
        //_shouldCloseDisplay.value = true
        // т.к. в это методе всегда ТРУ то можно установть UNIT. важен сам факт попадания сюда
        _shouldCloseDisplay.value = Unit
    }
}