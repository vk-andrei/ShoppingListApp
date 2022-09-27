package com.example.shoppinglistapp.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.domain.ShopItem

//class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {
class ShopListAdapter :
    ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var count = 0
    /*var shopList = listOf<ShopItem>()
        set(value) {
            val callback = ShopListDiffCallback(shopList, value)
            val diffResult = DiffUtil.calculateDiff(callback)
            //diffResult.dispatchUpdatesTo(this)
            diffResult.dispatchUpdatesTo(this)
            field = value
            //notifyDataSetChanged()
        }*/

    //var onItemLongClickListener: IonItemLongClickListener? = null
    // т.к. интерфейс - функционльный, то можно так: (без него)
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layout = when (viewType) {
            VIEW_TYPE_ENABLE -> R.layout.item_shop_enabled
            VIEW_TYPE_DISABLE -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Unknown view type $viewType")
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder: count = ${++count}")

        //val shopItem = shopList[position]
        val shopItem = getItem(position)

        holder.tvName.text = shopItem.name
        holder.tvQty.text = shopItem.qty.toString()

        holder.view.setOnLongClickListener {
            //onItemLongClickListener?.onItemLongClick(shopItem)
            onShopItemLongClickListener?.invoke(shopItem)
            true
        }
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(shopItem)
        }
    }
/** Этот метод теперь скрыт внутри ListAdapter-а **/
/*    override fun getItemCount(): Int {
        return shopList.size
    }*/

    override fun getItemViewType(position: Int): Int {
        //val item = shopList[position]
        val item = getItem(position)
        return if (item.enabled) {
            VIEW_TYPE_ENABLE
        } else {
            VIEW_TYPE_DISABLE
        }
    }

    companion object {
        const val VIEW_TYPE_DISABLE = 0
        const val VIEW_TYPE_ENABLE = 1
        const val MAX_POOL_SIZE = 30
    }

    /*interface IonItemLongClickListener {
        fun onItemLongClick(shopItem: ShopItem)
    }*/
}