package ma.pam.ajitsowak.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.adapter.HomeSliderAdapter
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.MyApp.getWooApi

import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.room.CartItem
import ma.pam.ajitsowak.ui.activity.*
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_CART_COUNT
import ma.pam.ajitsowak.utils.Constants.viewAllCode.FEATURED
import ma.pam.ajitsowak.utils.Constants.viewAllCode.MOSTRATE
import ma.pam.ajitsowak.utils.Constants.viewAllCode.POPULAR
import ma.pam.ajitsowak.utils.Constants.viewAllCode.RANDOM
import ma.pam.ajitsowak.utils.Constants.viewAllCode.SALE
import ma.pam.ajitsowak.woolib.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment1 : Fragment() {
    private var mMenuCart: View? = null
    private var image: String = ""
    private lateinit var lan: String
    private var count:Int = 0
    private lateinit var mViewNewest: View
    private lateinit var mViewFeatured: View
    private lateinit var mViewPopular: View
    private lateinit var mViewRand: View
    private lateinit var mViewRecentProduct: View
    private lateinit var mViewOnSale: View
    private lateinit var mViewMostRate: View
    private lateinit var mSliderView: View

    private lateinit var mLLDynamic: LinearLayout


    private fun setProductItem(view: View, model: Product, params: Boolean = false) {
        if (!params) {
            view.findViewById<ImageView>(R.id.ivProduct).layoutParams = activity?.productLayoutParams()
        } else {
            view.findViewById<ImageView>(R.id.ivProduct).layoutParams = activity?.productLayoutParamsForDealOffer()
        }
        if (model.images[0].src!!.isNotEmpty()) {
            view.findViewById<ImageView>(R.id.ivProduct).loadImageFromUrl(model.images[0].src!!)
            image = model.images[0].src!!
        }

        val mName = model.name.split(",")
        view.findViewById<TextView>(R.id.tvProductName).text = mName[0]
        //view.findViewById<TextView>(R.id.tvProductName).changeTextPrimaryColor()

        if (!model.isOn_sale) {
            view.findViewById<TextView>(R.id.tvDiscountPrice).text = model.price.plus(" MAD")
            view.findViewById<TextView>(R.id.tvOriginalPrice).visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.tvSaleLabel).visibility = View.GONE
            view.findViewById<TextView>(R.id.tvOriginalPrice).text = ""
        } else {
            if (model.sale_price.isNotEmpty()) {
                view.findViewById<TextView>(R.id.tvSaleLabel).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.tvDiscountPrice).text = model.sale_price.plus(" MAD")
                view.findViewById<TextView>(R.id.tvOriginalPrice).applyStrike()
                view.findViewById<TextView>(R.id.tvOriginalPrice).text = model.regular_price.plus(" MAD")
                view.findViewById<TextView>(R.id.tvOriginalPrice).visibility = View.VISIBLE
            } else {
                view.findViewById<TextView>(R.id.tvSaleLabel).visibility = View.GONE
                view.findViewById<TextView>(R.id.tvOriginalPrice).visibility = View.VISIBLE
                if (model.regular_price.isEmpty()) {
                    view.findViewById<TextView>(R.id.tvOriginalPrice).text = ""
                    view.findViewById<TextView>(R.id.tvDiscountPrice).text = model.price.plus(" MAD")
                } else {
                    view.findViewById<TextView>(R.id.tvOriginalPrice).text = ""
                    view.findViewById<TextView>(R.id.tvDiscountPrice).text = model.regular_price.plus(" MAD")
                }
            }
        }
      //  view.findViewById<TextView>(R.id.tvOriginalPrice).changeTextSecondaryColor()
      //  view.findViewById<TextView>(R.id.tvDiscountPrice).changeTextPrimaryColor()
       // view.findViewById<TextView>(R.id.tvAdd).background.setTint(Color.parseColor(getAccentColor()))
        if (model.productAttributes.isNotEmpty()) {
            view.findViewById<TextView>(R.id.tvProductWeight).text = model.productAttributes[0].options!![0]
           // view.findViewById<TextView>(R.id.tvProductWeight).changeAccentColor()
        }
        if (model.stock_status.equals("instock")) {
            view.findViewById<TextView>(R.id.tvAdd).show()
        } else {
            view.findViewById<TextView>(R.id.tvAdd).hide()
        }
        if (!model.isPurchasable) {
            view.findViewById<TextView>(R.id.tvAdd).hide()
        } else {
            view.findViewById<TextView>(R.id.tvAdd).show()
        }


        view.setOnClickListener {

            startActivity(Intent(activity, ProductDetailActivity1::class.java).apply {
                putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)
                putExtra(Constants.KeyIntent.DATA, model)
            })


            }

        view.findViewById<TextView>(R.id.tvAdd).setOnClickListener {
            addToCart(model)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_home1, container, false)


   /* private fun mAddCart(model: StoreProductModel) {
            val requestModel = RequestModel()
            if (model.type == "variable") {
                requestModel.pro_id = model.variations!![0]
            } else {
                requestModel.pro_id = model.id
            }
            requestModel.quantity = 1

            (activity as AppBaseActivity).addItemToCart(requestModel, onApiSuccess = {
                isAddedToCart = true
                activity!!.fetchAndStoreCartData()
            })

    }*/

    private fun addToCart(product: Product) {
        val isItemAdded = getRoom().Dao().addToCart(CartItem(
                productId = product.id,
                productImage = product.images.first().src!!,
                quantity = 1,
                price = product.price,
                regular_price = product.regular_price,
                sale_price = product.sale_price

        ))
        if (isItemAdded != (-1).toLong()){
            getSharedPrefInstance().setValue(KEY_CART_COUNT, count+1)
            setCartCount()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

       val refreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
       val scrollView = view.findViewById<NestedScrollView>(R.id.scrollView)
       val dashboardMainView = view.findViewById<LinearLayout>(R.id.dashboardMainView)

        mSliderUI()
        mProductUI()
        mLLDynamic = view.findViewById(R.id.dashboardMainView)

        listAllProducts()

        refreshLayout.setOnRefreshListener {
            dashboardMainView!!.removeAllViews()
            listAllProducts()
            refreshLayout.isRefreshing = false
        }
        refreshLayout.viewTreeObserver.addOnScrollChangedListener {
            refreshLayout.isEnabled = scrollView.scrollY == 0
        }

       // scrollView.changeBackgroundColor()
    }

    private fun mProductUI() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        mViewNewest = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewPopular  = inflater.inflate(R.layout.dashboard_productlist, null)

        mViewFeatured = inflater.inflate(R.layout.dashboard_dealofferview, null)

        mViewRand = inflater.inflate(R.layout.dashboard_productlist, null)
        mViewRecentProduct = inflater.inflate(R.layout.dashboard_productlist, null)

        mViewOnSale = inflater.inflate(R.layout.dashboard_dealofferview, null)

        mViewMostRate = inflater.inflate(R.layout.dashboard_productlist, null)
    }

    private fun mSliderUI() {
        val inflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mSliderView = inflater.inflate(R.layout.dashboard_sliderview, null)
    }

     //@Synchronized
     private fun onAddView(mView: View?, isGridView: Boolean = false, title: String, mViewAll: String, code: Int, specialKey: String = "", productList: List<Product>, modelSize: Int = 5) {
        val recyclerView = mView!!.findViewById(R.id.rvNewProduct) as RecyclerView
        val viewAllProduct = mView.findViewById(R.id.viewAllItem) as TextView
        val titleProduct = mView.findViewById(R.id.tvTitleBar) as TextView

      /*
      color
      if (mView == mViewDealOfTheDay || mView == mViewOffer) {
            mView.llDeal.changeTint(getPrimaryColor())
            mView.viewAllItem.changeBackgroundTint(getAccentColor())
            titleProduct.changeTitleColor()
        } else {
            mView.viewAllItem.changeTextSecondaryColor()
            titleProduct.changeAccentColor()
        }*/
        
        mView.findViewById<TextView>(R.id.viewAllItem).text = mViewAll
        titleProduct.text = title

        if (isGridView) {
            val productAdapter = BaseAdapter<Product>(R.layout.item_viewproductgrid, onBind = { view, model, _ ->
                setProductItem(view, model,isGridView)
            })

            productAdapter.addItems(productList)
            productAdapter.setModelSize(modelSize)

            recyclerView.setNestedScrollingEnabled(false)
            recyclerView.adapter = productAdapter
            recyclerView.layoutManager = GridLayoutManager(activity, 2)
            recyclerView.setHasFixedSize(true)


        } else {
            val productAdapter = BaseAdapter<Product>(R.layout.item_home_dashboard1, onBind = { view, model, _ ->
                    setProductItem(view, model)
                })
            productAdapter.addItems(productList)
            productAdapter.setModelSize(modelSize)

            recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

            recyclerView.adapter = productAdapter
            productAdapter.setModelSize(4)
        }

        viewAllProduct.setOnClickListener {
            val intent = Intent(activity,ViewAllProductActivity::class.java)
            intent.putExtra(Constants.KeyIntent.TITLE, title)
            intent.putExtra(Constants.KeyIntent.VIEWALLID, code)
            startActivity(intent)

        }
       /* if (mView.parent != null) {
            (mView.parent as ViewGroup).removeView(mView) // <- fix
        }*/
    }

    private fun addSlider(productList: List<String>) {
        val slideViewPager = mSliderView.findViewById(R.id.slideViewPager) as ViewPager
        //val dots = mSliderView!!.findViewById(R.id.dots) as DotsIndicator

        val adapter1 = HomeSliderAdapter(productList)
        slideViewPager.adapter = adapter1
        //dots.attachViewPager(slideViewPager)
        //dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)
        slideViewPager.pageMargin = resources.getDimensionPixelOffset(R.dimen._6sdp)
       // slideViewPager.setPageTransformer(false, object : CarouselEffectTransformer(activity) {})

        mLLDynamic.addView(mSliderView)
    }
   /* private fun loadApis() {
        if (isNetworkAvailable()) {
            activity!!.fetchAndStoreCartData()
        }
    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_dashboard, menu)

        val menuCartItem: MenuItem = menu.findItem(R.id.action_cart)
        menuCartItem.isVisible = true
        mMenuCart = menuCartItem.actionView

        mMenuCart?.setOnClickListener {
            startActivity(Intent(activity,MyCartActivity::class.java))
        }
        val item = menu.findItem(R.id.action_search)
       // icon.setColorFilter(Color.parseColor(getTextTitleColor()), PorterDuff.Mode.SRC_IN)
        setCartCount()
    }

    override fun onResume() {
        super.onResume()
        setCartCount()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(activity,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setCartCount() {
        count = getCartCount()
       // mMenuCart?.ivCart?.changeBackgroundImageTint(getTextTitleColor())
       // mMenuCart?.tvNotificationCount?.changeTint(getTextTitleColor())
        val tvNotificationCount: TextView? = mMenuCart?.findViewById(R.id.tvNotificationCount)
        tvNotificationCount?.text = count.toString()
       // tvNotificationCount?.changeAccentColor()
        if (count == 0) {
            tvNotificationCount?.hide()
        } else {
            tvNotificationCount?.show()
        }
    }

    private fun listAllProducts() {
        if (isNetworkAvailable()) {
            addSlider(listOf("https://image.freepik.com/free-vector/best-sale-banner-origami-style_23-2148386593.jpg",
                    "https://image.freepik.com/free-vector/abstract-colorful-best-sale-banner_23-2148340539.jpg",
                    "https://image.freepik.com/vecteurs-libre/collection-etiquettes-best-sale_41577-131.jpg")
            )

            mLLDynamic.addView(mViewNewest)
            mLLDynamic.addView(mViewPopular)
            mLLDynamic.addView(mViewFeatured )
            mLLDynamic.addView(mViewRand)
            mLLDynamic.addView(mViewOnSale)
            mLLDynamic.addView(mViewMostRate)

            mViewNewest.visibility = GONE
            mViewPopular.visibility = GONE
            mViewFeatured.visibility = GONE
            mViewRand.visibility = GONE
            mViewOnSale.visibility = GONE
            mViewMostRate.visibility = GONE

            (activity as mAppCompatActivity).showProgress(true)

            getWooApi().getNewProducts(1,5).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()) {
                        onAddView(mView = mViewNewest, title = "New Product", mViewAll = "ViewAll", code = Constants.viewAllCode.NEWEST, productList = response.body()!!, modelSize = 5)
                        mViewNewest.visibility = VISIBLE
                    }
                }
            })

            getWooApi().getPopularProducts(1,5).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()) {
                        onAddView(mView = mViewPopular, title = "Popular", mViewAll = "ViewAll", code = POPULAR, productList = response.body()!!, modelSize = 5)
                        mViewPopular.visibility = VISIBLE
                    }
                }
            })

            getWooApi().getFeaturedProduct(1,6).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()) {
                        onAddView(mView = mViewFeatured, isGridView = true, title = "Featured", mViewAll = "ViewAll", code = FEATURED, productList = response.body()!!, modelSize = 5)
                        mViewFeatured.visibility = VISIBLE
                    }

                }
            })

            getWooApi().getRandomProduct(1,5).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()) {
                        onAddView(mView = mViewRand, title = "You May Also Like", mViewAll = "ViewAll", code = RANDOM, productList = response.body()!!, modelSize = 5)
                        mViewRand.visibility = VISIBLE
                    }
                }
            })

            //onAddView(mView = mViewRecentProduct, title = "titel new", mViewAll = "chof kolchi", code = Constants.viewAllCode.NEWEST, productList = response.body()!!, modelSize = 5)mLLDynamic!!.addView(mViewNewest!!)

            getWooApi().getOnSaleProduct(1,6).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()) {
                        var product = response.body()!!

                        onAddView(mView = mViewOnSale, isGridView = true, title = "On Sale", mViewAll = "ViewAll", code = SALE, productList = response.body()!!, modelSize = 5)
                        mViewOnSale.visibility = VISIBLE
                    }
                }
            })
            getWooApi().getMostRateProduct(1,5).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    (activity as mAppCompatActivity).showProgress(false)
                }
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (!response.body().isNullOrEmpty()){
                        onAddView(mView = mViewMostRate, title = "Most Rate", mViewAll = "ViewAll", code = MOSTRATE, productList = response.body()!!, modelSize = 5)
                        mViewMostRate.visibility = VISIBLE
                    }
                    (activity as mAppCompatActivity).showProgress(false)
                }
            })
        }
    }
    /*private fun setNewLocale(language: String) {
        MyApp.changeLanguage(language)
    }*/
}