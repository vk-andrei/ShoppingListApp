package com.example.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.ActivityShopItemBinding
import com.example.shoppinglistapp.domain.ShopItem

class ShopItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShopItemBinding
    private lateinit var viewModel: ShopItemViewModel
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShopItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        parseIntent()
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchRightMode()
        observeViewModel()
        addTextChangeListeners()
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }

        viewModel.errorInputQty.observe(this) {
            val message = if (it) {
                getString(R.string.error_input_qty)
            } else {
                null
            }
            binding.tilQty.error = message
        }

        viewModel.shouldCloseDisplay.observe(this) {
            finish()
        }
    }

    private fun addTextChangeListeners() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.etQty.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputQty()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchAddMode() = with(binding) {
        btnSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etQty.text?.toString())
        }
    }

    private fun launchEditMode() = with(binding) {
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this@ShopItemActivity) {
            etName.setText(it.name)
            etQty.setText(it.qty.toString())
        }
        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etQty.text?.toString())
        }
    }


    // Checking intent - is it OK?
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