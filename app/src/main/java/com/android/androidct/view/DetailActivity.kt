package com.android.androidct.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.android.androidct.R
import com.android.androidct.injection.CTApplication
import com.android.androidct.repo.LocalPreferences
import com.android.androidct.repo.entities.Item
import com.android.androidct.view.adapters.ItemListAdapter
import com.squareup.picasso.Picasso
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
/**
 * Created by xiaomei on 23/2/18.
 */
class DetailActivity : RxAppCompatActivity(){
    companion object {
        val TAG = DetailActivity::class.simpleName
        val EXTRA_ITEM = "extra_item"

        fun newInstance(context: Context, item: Item): Intent {
            var intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_ITEM, item)
            return intent
        }
    }

    @Inject
    lateinit var localPreferences: LocalPreferences

    @Inject
    lateinit var picasso: Picasso

    @BindView(R.id.image)
    lateinit var imageView: ImageView

    @BindView(R.id.name)
    lateinit var nameView : TextView

    @BindView(R.id.desc)
    lateinit var descView : TextView

    @BindView(R.id.avg_price)
    lateinit var avgPriceView : TextView

    @BindView(R.id.rating)
    lateinit var ratingView : TextView

    lateinit var itemListAdapter: ItemListAdapter
    var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        CTApplication.graph.inject(this)
        ButterKnife.bind(this)
        item = intent.getParcelableExtra(EXTRA_ITEM)
        item?.let {
        } ?: kotlin.run {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        displayData()
    }

    private fun displayData() {
        item?.let {
            picasso.load(it.imageUrl).into(imageView)
            nameView.text = it.name
            descView.text = it.description
            avgPriceView.text = resources.getString(R.string.label_price, it.averagePrice)
            ratingView.text = resources.getString(R.string.label_rating, it.rating)
        }
    }
}
