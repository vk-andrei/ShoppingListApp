package com.example.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.domain.ShopItem
import java.lang.RuntimeException

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var shopList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

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

        val shopItem = shopList[position]

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

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.enabled) {
            VIEW_TYPE_ENABLE
        } else {
            VIEW_TYPE_DISABLE
        }
    }

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById<TextView>(R.id.tv_name)
        val tvQty: TextView = view.findViewById<TextView>(R.id.tv_qty)
    }

    companion object {
        const val VIEW_TYPE_DISABLE = 0
        const val VIEW_TYPE_ENABLE = 1
        const val MAX_POOL_SIZE = 15
    }

    /*interface IonItemLongClickListener {
        fun onItemLongClick(shopItem: ShopItem)
    }*/
}