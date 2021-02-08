package ma.pam.ajitsowak.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.woolib.models.Category
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryActivity : mAppCompatActivity() {
    private var showPagination: Boolean? = true
    private var mIsLoading = false
    private var countLoadMore = 1
    private var isLastPage: Boolean? =false

    var TOTAL_CATEGORY_PER_PAGE = 8

    private val mProductAdapter = BaseAdapter<Category>(R.layout.item_viewcat, onBind = { view, model, _ ->

        val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)

            if (model.image?.src != null) {
                ivProduct.loadImageFromUrl(model.image?.src!!)
                ivProduct.visibility = View.VISIBLE
            } else {
                ivProduct.loadImageFromDrawable(R.drawable.app_logo)
            }
            tvProductName.text = model.name
            //tvProductName.changeTextPrimaryColor()
            view.setOnClickListener {
                startActivity(Intent(this@CategoryActivity,ViewAllProductActivity::class.java).apply {
                    putExtra(Constants.KeyIntent.TITLE, model.name)
                    putExtra(Constants.KeyIntent.VIEWALLID, Constants.viewAllCode.CATEGORY)
                    putExtra(Constants.KeyIntent.KEYID, model.id) })
            }
        })

    lateinit var toolbar: Toolbar
    lateinit var rvNewestProduct:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        toolbar = findViewById(R.id.toolbar)

        rvNewestProduct = findViewById(R.id.rvNewestProduct)

        setDetailToolbar(toolbar)
        title = getString(R.string.lbl_category)

        //mAppBarColor()
        changeColor()
        loadData()

        rvNewestProduct.apply {
            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            setHasFixedSize(true)
            adapter = mProductAdapter
            rvItemAnimation()
            if (showPagination!!) {
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                        super.onScrollStateChanged(recyclerView, newState)
                        val countItem = recyclerView.layoutManager?.itemCount

                        var lastVisiblePosition = 0
                        if (recyclerView.layoutManager is LinearLayoutManager) {
                            lastVisiblePosition =
                                (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                        } else if (recyclerView.layoutManager is GridLayoutManager) {
                            lastVisiblePosition =
                                (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                        }
                        if (isLastPage==false) {
                            if (lastVisiblePosition != 0 && !mIsLoading && countItem?.minus(1) == lastVisiblePosition) {
                                mIsLoading = true
                                countLoadMore = countLoadMore.plus(1)

                                loadData()
                            }
                        }
                    }
                })
            }
        }
    }

    private fun loadData() {
        if (isNetworkAvailable()) {
            showProgress(true)
            getWooApi().getAllCategory(countLoadMore,8).enqueue(object : Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                    showProgress(false)
                }
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {

                    if (countLoadMore == 1) {
                        mProductAdapter.clearItems()
                    }
                    if(response.body().isNullOrEmpty()){
                        isLastPage=true
                    }else{
                        mIsLoading = false
                        mProductAdapter.addMoreItems(response.body()!!)
                    }
                    if (mProductAdapter.itemCount == 0) {
                        rvNewestProduct.hide()
                    } else {
                        rvNewestProduct.show()
                    }
                    showProgress(false)
                }
            })
        }
    }
    private fun changeColor()
    {
     //   llMain.changeBackgroundColor()
    }

}
