package com.android.androidct.view

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.android.androidct.R
import com.android.androidct.injection.CTApplication
import com.android.androidct.repo.APIClient
import com.android.androidct.repo.LocalPreferences
import com.android.androidct.view.adapters.ItemListAdapter
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Created by xiaomei on 23/2/18.
 */
class MainActivity : RxAppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, ItemListAdapter.OnItemClickListener {
    override fun onItemClicked(index: Int) {
        startActivity(DetailActivity.newInstance(this, localPreferences.items[index]))
    }

    override fun onRefresh() {
        fetchLatestItems()
    }

    companion object {
        val TAG = MainActivity::class.simpleName
    }

    @Inject
    lateinit var localPreferences: LocalPreferences

    @Inject
    lateinit var apiClient: APIClient

    @Inject
    lateinit var picasso: Picasso

    @BindView(R.id.swpie_layout)
    lateinit var swipeLayout: SwipeRefreshLayout

    @BindView(android.R.id.list)
    lateinit var list : RecyclerView

    lateinit var itemListAdapter: ItemListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CTApplication.graph.inject(this)
        ButterKnife.bind(this)
        swipeLayout.setOnRefreshListener(this)
        itemListAdapter = ItemListAdapter(picasso)
        itemListAdapter.onItemClickListener = this
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = itemListAdapter
    }

    override fun onResume() {
        super.onResume()
        displayCachedData()
        fetchLatestItems()
    }

    private fun displayCachedData() {
        localPreferences.cache
        itemListAdapter.items.clear()
        itemListAdapter.items.addAll(localPreferences.items)
        itemListAdapter.notifyDataSetChanged()
    }

    private fun fetchLatestItems() {
        swipeLayout.isRefreshing = true
        apiClient
                .listItems()
                .compose(bindUntilEvent(ActivityEvent.PAUSE))
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally({
                    displayCachedData()
                    swipeLayout.isRefreshing = false
                })
                .subscribe({
                    Toast.makeText(this, "Successfully fetched data", Toast.LENGTH_SHORT).show()
                }, {
                    Log.d(TAG, it.message, it)
                    Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this, "Unexpected error", Toast.LENGTH_SHORT).show()
                })
    }
}
