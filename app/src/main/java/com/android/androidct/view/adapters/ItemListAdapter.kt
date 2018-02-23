package com.android.androidct.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.android.androidct.R
import com.android.androidct.repo.entities.Item
import com.squareup.picasso.Picasso

/**
 * Created by xiaomei on 23/2/18.
 */
class ItemListAdapter(var picasso: Picasso) : RecyclerView.Adapter<ItemListAdapter.ItemViewHolder>() {
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var item = items[position]
        holder.title.text = item.name
        holder.subtitle.text = item.description
        picasso.cancelRequest(holder.image)
        picasso.load(item.imageUrl).into(holder.image)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    var onItemClickListener: OnItemClickListener? = null
    var items = mutableListOf<Item>()

    class ItemViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        @BindView(R.id.title)
        lateinit var title: TextView

        @BindView(R.id.subtitle)
        lateinit var subtitle: TextView

        @BindView(R.id.image)
        lateinit var image: ImageView

        init {
            ButterKnife.bind(this, view)
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(index: Int)
    }
}