package com.example.shoppinglistapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.support.v7.widget.helper.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.R

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            shopListAdapter.shopList = it
        }
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = shopListAdapter
            // сами ставим сколько ВЬЮШЕК будет создаваться
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLE,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }

        /*shopListAdapter.onItemLongClickListener = object : ShopListAdapter.IonItemLongClickListener {
            override fun onItemLongClick(shopItem: ShopItem) {
                viewModel.changeEnableState(shopItem)
            }
        }*/
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }

        shopListAdapter.onShopItemClickListener = {
            Log.d("MainAct", "shopListAdapter.onShopItemClickListener: item - $it")
        }

        /** SWIPE TO DELETE ********************************************************/
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, p1: Int) {
                val item = shopListAdapter.shopList[viewHolder.adapterPosition]
                viewModel.removeShopItem(item)
                shopListAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)

        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

}