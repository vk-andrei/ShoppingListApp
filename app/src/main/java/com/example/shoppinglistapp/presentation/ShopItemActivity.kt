package com.example.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.ActivityShopItemBinding
import com.example.shoppinglistapp.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        val fragmentInRightMode = when (screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAddShopItem()
            MODE_EDIT -> ShopItemFragment.newInstanceEditShopItem(shopItemId)
            else -> throw java.lang.RuntimeException("Unknown screen mode $screenMode")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_for_shop_item, fragmentInRightMode)
            .commit()
    }

    companion object {
        private const val EXTRA_SHOP_ITEM_ID = "EXTRA_SHOP_ITEM_ID"
        private const val EXTRA_SCREEN_MODE = "EXTRA_SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""

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