package ma.pam.ajitsowak.ui.activity

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.slider.RangeSlider
import ma.pam.ajitsowak.MyApp
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.room.CartItem
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.woolib.models.Product
import ma.pam.ajitsowak.woolib.models.filters.ProductFilter
import ma.pam.ajitsowak.woolib.models.filters.Sort

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : mAppCompatActivity() {
    private var mSearchQuery = ""
    private var mPage = 1
    private var mIsLoading = false
    private var productFilter = ProductFilter()
    private var isLastPage: Boolean = false

    private val mProductAdapter = BaseAdapter<Product>(R.layout.item_viewproductgrid, onBind = { view, model, _ ->
            val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
            val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
            val tvDiscountPrice = view.findViewById<TextView>(R.id.tvDiscountPrice)
            val tvOriginalPrice = view.findViewById<TextView>(R.id.tvOriginalPrice)
            val tvProductWeight = view.findViewById<TextView>(R.id.tvProductWeight)
            val tvAdd = view.findViewById<TextView>(R.id.tvAdd)
            val tvSaleLabel = view.findViewById<TextView>(R.id.tvSaleLabel)

            if (model.images[0].src!!.isNotEmpty()) {
                ivProduct.loadImageFromUrl(model.images[0].src!!)
            }

            val mName = model.name.split(",")

            tvProductName.text = mName[0]
            //tvProductName.changeTextPrimaryColor()
            if (!model.isOn_sale) {
                tvDiscountPrice.text = model.price.currencyFormat()
                tvOriginalPrice.visibility = View.VISIBLE
                tvOriginalPrice.text = ""
            } else {
                if (model.sale_price.isNotEmpty()) {
                    tvDiscountPrice.text = model.sale_price.currencyFormat()
                    tvOriginalPrice.applyStrike()
                    tvOriginalPrice.text = model.regular_price.currencyFormat()
                    tvOriginalPrice.visibility = View.VISIBLE
                    tvSaleLabel.visibility = View.VISIBLE
                } else {
                    tvOriginalPrice.visibility = View.VISIBLE
                    if (model.regular_price.isEmpty()) {
                        tvOriginalPrice.text = ""
                        tvDiscountPrice.text = model.price.currencyFormat()
                    } else {
                        tvOriginalPrice.text = ""
                        tvDiscountPrice.text = model.regular_price.currencyFormat()
                    }
                }
            }
           // tvOriginalPrice.changeTextSecondaryColor()
           // tvDiscountPrice.changeTextPrimaryColor()
            //tvAdd.changeBackgroundTint(getAccentColor())
            if (model.productAttributes.isNotEmpty()) {
                tvProductWeight.text = model.productAttributes[0].options!![0]
            }
            if (model.stock_status.equals("instock")) {
                tvAdd.show()
            } else {
                tvAdd.hide()
            }
            if (!model.isPurchasable) {
                tvAdd.hide()
            } else {
                tvAdd.show()
            }
            view.setOnClickListener {
                startActivity(Intent(this,ProductDetailActivity1::class.java).apply {
                    putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)
                    putExtra(Constants.KeyIntent.DATA, model)
                })
            }
            tvAdd.setOnClickListener {
                addToCart(model)
            }
        })

    private fun addToCart(product: Product) {
        val isItemAdded = MyApp.getRoom().Dao().addToCart(CartItem(
                productId = product.id,
                productImage = product.images.first().src!!,
                quantity = 1,
                price = product.price,
                regular_price = product.regular_price,
                sale_price = product.sale_price
        ))
        if (isItemAdded != (-1).toLong()){
            val count = getSharedPrefInstance().getIntValue(Constants.SharedPref.KEY_CART_COUNT,0)
            getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, count+1)
        }
    }

    lateinit var toolbar:Toolbar
    lateinit var searchView:SearchView
    lateinit var aSearch_rvSearch:RecyclerView
    lateinit var rlNoData:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        toolbar = findViewById(R.id.toolbar)
        searchView = findViewById(R.id.searchView)
        aSearch_rvSearch = findViewById(R.id.aSearch_rvSearch)
        rlNoData = findViewById(R.id.rlNoData)

        toolbar.title = ""
        toolbar.navigationIcon!!.setColorFilter(resources.getColor(R.color.colorBackArrow), PorterDuff.Mode.SRC_ATOP)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        setToolbar(toolbar)
        //mAppBarColor()
        //findViewById<LinearLayout>(R.id.llMain).changeBackgroundColor()

        productFilter.setPer_page(8)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mPage = 1
                mSearchQuery = query!!
                productFilter.setSearch(mSearchQuery)
                productFilter.setPage(mPage)

                loadProducts()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        searchView.setOnCloseListener {
            Toast.makeText(this,"on close",Toast.LENGTH_LONG).show()
            true
        }

        val searchEditText = searchView.findViewById<EditText>(R.id.search_src_text)
        searchEditText.setTextColor(resources.getColor(R.color.white))
        searchEditText.setHintTextColor(resources.getColor(R.color.white))
        searchView.onActionViewExpanded()

        aSearch_rvSearch.apply {
            adapter = mProductAdapter
            layoutManager = GridLayoutManager(this@SearchActivity, 2)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val countItem = recyclerView.layoutManager?.itemCount

                    val lastVisiblePosition = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                    if (!isLastPage)
                    if (lastVisiblePosition != 0 && !mIsLoading && countItem?.minus(1) == lastVisiblePosition) {
                        mIsLoading = true
                        mPage = mPage.plus(1)
                        productFilter.setPage(mPage)
                        loadProducts()
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
       // icon.setColorFilter(Color.parseColor(getTextTitleColor()), PorterDuff.Mode.SRC_IN)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed();
                true
            }
            R.id.action_filter -> {
                    openFilterBottomSheet()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadProducts() {

        if (isNetworkAvailable()) {
            showProgress(true)
            getWooApi().getFilterProduct(productFilter).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    if (mPage == 1) {
                        mProductAdapter.clearItems()
                    }
                    rlNoData.show()
                    showProgress(false)
                }
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (mPage == 1) {
                        mProductAdapter.clearItems()
                    }
                    mIsLoading = false
                    if (!response.body().isNullOrEmpty()) {
                        mProductAdapter.addMoreItems(response.body()!!)
                    } else  {
                        isLastPage = true
                    }
                    if (mProductAdapter.itemCount == 0) {
                        rlNoData.show()
                        aSearch_rvSearch.hide()
                    } else {
                        rlNoData.hide()
                        aSearch_rvSearch.show()
                    }
                    showProgress(false)
                }
            })
        }
    }

    private fun openFilterBottomSheet() {
        val filterDialog = BottomSheetDialog(this)
        filterDialog.setContentView(R.layout.layout_filter)
        filterDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

       // val lblFilter:TextView = filterDialog.findViewById(R.id.lblFilter)
       // val tvAttributesName:TextView = filterDialog.findViewById(R.id.tvAttributesName)
        val tvApply = filterDialog.findViewById<TextView>(R.id.tvApply)
       // val lblMin:TextView = filterDialog.findViewById(R.id.lblMin)
       // val lblMax:TextView = filterDialog.findViewById(R.id.lblMax)
        val ivClose = filterDialog.findViewById<ImageView>(R.id.ivClose)
        val rangebar = filterDialog.findViewById<RangeSlider>(R.id.rangebar)
        val tvReset = filterDialog.findViewById<TextView>(R.id.tvReset)

        val toggleorderby = filterDialog.findViewById<MaterialButtonToggleGroup>(R.id.orderby)
        val toggletype = filterDialog.findViewById<MaterialButtonToggleGroup>(R.id.type)
        val toggleorder = filterDialog.findViewById<MaterialButtonToggleGroup>(R.id.order)

        //lblFilter.changeTextPrimaryColor()
       // tvAttributesName.changeTint(getAccentColor())
       // tvApply.changeTint(getPrimaryColor())
       // lblMin.changeTextPrimaryColor()
       // lblMax.changeTextPrimaryColor()
       // ivClose.changeBackgroundImageTint(getPrimaryColor())

        val max = 500f
        rangebar?.valueTo = max

        if (productFilter.min_price.isNullOrEmpty()&& productFilter.max_price.isNullOrEmpty())
            rangebar?.values = listOf(0f, max)
        else
            rangebar?.values = listOf(productFilter.min_price?.toFloat(), productFilter.max_price?.toFloat())
        /*
        rangebar?.values?.set(0, productFilter.min_price?.toFloat())
        rangebar?.values?.set(1,productFilter.max_price?.toFloat())*/

        rangebar?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {}
            override fun onStopTrackingTouch(slider: RangeSlider) {
                productFilter.min_price = slider.values[0].toString()
                productFilter.max_price = slider.values[1].toString()
            }
        })

        when(productFilter.getOrderby()){
            "date"->toggleorderby?.check(R.id.newest)
            "price"->toggleorderby?.check(R.id.price)
            "popularity"->toggleorderby?.check(R.id.popular)
            "rating"->toggleorderby?.check(R.id.mostrate)
        }

        toggleorderby?.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked)
            when(checkedId){
                R.id.newest-> productFilter.setOrderby("date")
                R.id.price-> productFilter.setOrderby("price")
                R.id.popular-> productFilter.setOrderby("popularity")
                R.id.mostrate-> productFilter.setOrderby("rating")
            }
        }

        if (!(productFilter.isOn_sale || productFilter.isFeatured))
            toggletype?.check(R.id.all)
        else if(productFilter.isOn_sale)
            toggletype?.check(R.id.onsale)
        else
            toggletype?.check(R.id.featured)

        toggletype?.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked)
            when(checkedId){
                R.id.all->{
                    productFilter.isOn_sale=false
                    productFilter.isFeatured = false
                }
                R.id.onsale->{
                    productFilter.isOn_sale=true
                    productFilter.isFeatured = false
                }
                R.id.featured->{
                    productFilter.isFeatured = true
                    productFilter.isOn_sale = false
                }
            }
        }

        when(productFilter.getOrder()){
            Sort.DESCENDING.toString() -> toggleorder?.check(R.id.des)
            Sort.ASCENDING.toString() ->  toggleorder?.check(R.id.asc)
        }

        toggleorder?.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked)
            when(checkedId){
                R.id.des-> productFilter.setOrder(Sort.DESCENDING)
                R.id.asc-> productFilter.setOrder(Sort.ASCENDING)
            }
        }

        tvApply?.setOnClickListener {
            mPage=1
            productFilter.setPage(mPage)
            loadProducts()
            filterDialog.dismiss()
        }

        tvReset?.setOnClickListener {
            searchView.setQuery("", false);
            productFilter = ProductFilter()
            loadProducts()
            filterDialog.dismiss()
        }


        ivClose?.setOnClickListener {
            filterDialog.dismiss()
        }

        filterDialog.show()
    }


}
