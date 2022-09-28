package com.example.shoppinglistapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.R

import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.shopList.observe(this) {
            //shopListAdapter.shopList = it
            shopListAdapter.submitList(it)
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab_add_shop_item)
        fab.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this@MainActivity)
            startActivity(intent)
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

        setupLongClickListener()
        setupClickListener()
        setupSwipeListener(rvShopList)

    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            viewModel.changeEnableState(it)
        }
    }

    private fun setupClickListener() {
        shopListAdapter.onShopItemClickListener = {
            val intent = ShopItemActivity.newIntentEditItem(this@MainActivity, it.id)
            startActivity(intent)
        }
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
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

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //val item = shopListAdapter.shopList[viewHolder.adapterPosition]
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.removeShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

}