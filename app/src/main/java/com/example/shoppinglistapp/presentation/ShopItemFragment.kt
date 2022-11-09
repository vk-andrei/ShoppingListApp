package com.example.shoppinglistapp.presentation

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentShopItemBinding
import com.example.shoppinglistapp.domain.ShopItem

class ShopItemFragment : Fragment() {

    private var _binding: FragmentShopItemBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ShopItemViewModel
    private var screenMode: String = MODE_UNKNOWN
    private var shopItemId: Int = ShopItem.UNDEFINED_ID
    private lateinit var onFinishedEditingListener: OnFinishedEditingListener

    override fun onAttach(context: Context) {
        Log.d("FRAG", "FRAGMENT: onAttach")
        super.onAttach(context)
        if (context is OnFinishedEditingListener) {
            onFinishedEditingListener = context
        } else {
            throw RuntimeException("Activity must implement: OnFinishedEditingListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FRAG", "FRAGMENT: onCreate")
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("FRAG", "FRAGMENT: onCreateView")
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FRAG", "FRAGMENT: onViewCreated")

        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        launchRightMode()
        observeViewModel()
        addTextChangeListeners()
    }

    override fun onStart() {
        super.onStart()
        Log.d("FRAG", "FRAGMENT: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("FRAG", "FRAGMENT: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("FRAG", "FRAGMENT: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("FRAG", "FRAGMENT: onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("FRAG", "FRAGMENT: onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("FRAG", "FRAGMENT: onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("FRAG", "FRAGMENT: onDetach")
    }

    private fun parseParams() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (mode == MODE_EDIT) {
            if (!args.containsKey(SHOP_ITEM_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchRightMode() {
        when (screenMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_name)
            } else {
                null
            }
            binding.tilName.error = message
        }

        viewModel.errorInputQty.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_input_qty)
            } else {
                null
            }
            binding.tilQty.error = message
        }

        viewModel.shouldCloseDisplay.observe(viewLifecycleOwner) {
            onFinishedEditingListener.onFinishedEditing()
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
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etQty.setText(it.qty.toString())
        }
        btnSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etQty.text?.toString())
        }
    }

    interface OnFinishedEditingListener {
        fun onFinishedEditing()
    }

    companion object {
        private const val SHOP_ITEM_ID = "SHOP_ITEM_ID"
        private const val SCREEN_MODE = "SCREEN_MODE"
        private const val MODE_ADD = "MODE_ADD"
        private const val MODE_EDIT = "MODE_EDIT"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddShopItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun newInstanceEditShopItem(shopItemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID, shopItemId)
                }
            }
        }
    }
}