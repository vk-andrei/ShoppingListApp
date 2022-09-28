package com.example.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.shoppinglistapp.databinding.ActivityShopItemBinding

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }


    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "EXTRA_SHOP_ITEM_ID"
        private const val EXTRA_SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"

        fun newIntentAddItem(context: Context): Intent {
            val i = Intent(context, ShopItemActivity::class.java)
            i.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return i
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val i = Intent(context, ShopItemActivity::class.java)
            i.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            i.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return i
        }
    }
}