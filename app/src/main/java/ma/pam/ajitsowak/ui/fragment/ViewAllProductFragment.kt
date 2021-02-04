package ma.pam.ajitsowak.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.MyApp

import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.ui.activity.ViewAllProductActivity
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.viewAllCode.CATEGORY
import ma.pam.ajitsowak.utils.Constants.viewAllCode.FEATURED
import ma.pam.ajitsowak.utils.Constants.viewAllCode.MOSTRATE
import ma.pam.ajitsowak.utils.Constants.viewAllCode.NEWEST
import ma.pam.ajitsowak.utils.Constants.viewAllCode.POPULAR
import ma.pam.ajitsowak.utils.Constants.viewAllCode.RANDOM
import ma.pam.ajitsowak.utils.Constants.viewAllCode.SALE
import ma.pam.ajitsowak.woolib.models.Category
import ma.pam.ajitsowak.woolib.models.Product
import ma.pam.ajitsowak.woolib.models.filters.ProductFilter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ViewAllProductFragment : Fragment() {

    private var showPagination: Boolean? = true
    private var mIsLoading = false
    private var countLoadMore = 1
    private var subCategoryCountLoadMore = 1
    private var mCategoryId: Int = 0
    private var menuCart: View? = null
    private var mId: Int = 0
    private var mColorArray = intArrayOf(R.color.cat_1)
    private var productFilter = ProductFilter()

    private var isLastPage: Boolean? = false
    private var mIsLastPage: Boolean? = false


    private var TOTAL_ITEM_PER_PAGE=10
    private var TOTAL_SUB_CATEGORY_PER_PAGE=10

    companion object {
        fun getNewInstance(id: Int, mCategoryId: Int, showPagination: Boolean = true): ViewAllProductFragment {

            val fragment = ViewAllProductFragment()
            val bundle = Bundle()
            bundle.putSerializable(Constants.KeyIntent.VIEWALLID, id)
            bundle.putSerializable(Constants.KeyIntent.KEYID, mCategoryId)
            bundle.putSerializable(Constants.KeyIntent.SHOW_PAGINATION, showPagination)


            fragment.arguments = bundle
            return fragment
        }
    }

    private val mSubCategoryAdapter = BaseAdapter<Category>(R.layout.item_subcategory, onBind = { view, model, position ->

        val tvSubCategory = view.findViewById<TextView>(R.id.tvSubCategory)
        val ivProducts = view.findViewById<ImageView>(R.id.ivProducts)

            tvSubCategory.text = model.name
            if (!model.image?.src.isNullOrEmpty()) {
                    ivProducts.loadImageFromUrl(model.image?.src!!)
                    ivProducts.visibility = View.VISIBLE
            } else {
                ivProducts.visibility = View.GONE
            }
           // view.llMain.setStrokedBackground((activity as AppCompatActivity).color(R.color.transparent), (activity as AppCompatActivity).color(mColorArray[position % mColorArray.size]))

            //view.tvSubCategory.setTextColor((activity as AppCompatActivity).color(mColorArray[position % mColorArray.size]))

            view.setOnClickListener {
                startActivity(Intent(activity, ViewAllProductActivity::class.java).apply {
                    putExtra(Constants.KeyIntent.TITLE, model.name)
                    putExtra(Constants.KeyIntent.VIEWALLID,CATEGORY)
                    putExtra(Constants.KeyIntent.KEYID, model.id) })

               // activity.launchActivity<SubCategoryActivity> { putExtra(Constants.KeyIntent.TITLE, model.name)putExtra(Constants.KeyIntent.KEYID, model.id) }
            }
            //view.tvSubCategory.changeTextPrimaryColor()
           // view.llMain.setStrokedBackground(Color.parseColor(getTextPrimaryColor()), Color.parseColor(getTextPrimaryColor()), 0.4f)
        })

    private val mProductAdapter = BaseAdapter<Product>(R.layout.item_viewproductgrid, onBind = { view, model, _ ->

        val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvDiscountPrice = view.findViewById<TextView>(R.id.tvDiscountPrice)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tvOriginalPrice)
        val tvProductWeight = view.findViewById<TextView>(R.id.tvProductWeight)
        val tvAdd = view.findViewById<TextView>(R.id.tvAdd)


            if (model.images.isNotEmpty()) {
                if (model.images[0].src!!.isNotEmpty()) {
                    ivProduct.loadImageFromUrl(model.images[0].src!!)
                }
            } else {
                ivProduct.loadImageFromDrawable(R.drawable.app_logo)
            }

            val mName = model.name.split(",")

            tvProductName.text = mName[0]
          //  tvProductWeight.changePrimaryColor()
          //  tvProductName.changeTextPrimaryColor()
           // tvOriginalPrice.changeTextSecondaryColor()
           // tvDiscountPrice.changeTextPrimaryColor()
            //tvAdd.background.setTint(Color.parseColor(getAccentColor()))

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
                  /*  launchActivity<ProductDetailActivity1> {
                        putExtra(Constants.KeyIntent.PRODUCT_ID, model.id)
                        putExtra(Constants.KeyIntent.DATA, model)
                    }*/

            }
            tvAdd.setOnClickListener {
              //  addCart(model)
            }
        })


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_newest_product, container, false)
    }


    lateinit var rvNewestProduct:RecyclerView
    lateinit var rvCategory:RecyclerView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        rvNewestProduct = view.findViewById(R.id.rvNewestProduct)
        rvCategory = view.findViewById(R.id.rvCategory)

        mId = arguments?.getInt(Constants.KeyIntent.VIEWALLID)!!
        mCategoryId = arguments?.getInt(Constants.KeyIntent.KEYID)!!
        showPagination = arguments?.getBoolean(Constants.KeyIntent.SHOW_PAGINATION)

        rvNewestProduct.layoutManager = GridLayoutManager(activity, 2)
       // fLMain.changeBackgroundColor()

        if (mId == CATEGORY) {

            loadCategory()

            loadSubCategory()
        } else {

            loadData()
        }

        rvCategory.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            setHasFixedSize(true)
            rvCategory.adapter = mSubCategoryAdapter
            rvCategory.rvItemAnimation()
        }

        rvNewestProduct.apply {
            rvNewestProduct.rvItemAnimation()
            rvNewestProduct.adapter = mProductAdapter

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
                        if (mId == CATEGORY) {
                            if (isLastPage == false) {
                                if (lastVisiblePosition != 0 && !mIsLoading && countItem?.minus(1) == lastVisiblePosition) {
                                    mIsLoading = true
                                    countLoadMore = countLoadMore.plus(1)

                                    loadCategory()
                                }
                            }
                        } else {
                            if (lastVisiblePosition != 0 && !mIsLoading && countItem?.minus(1) == lastVisiblePosition ) {
                                mIsLoading = true
                                countLoadMore = countLoadMore.plus(1)
                              /*  searchRequest.page = countLoadMore
                                searchRequest.product_per_page=TOTAL_ITEM_PER_PAGE*/
                                loadData()
                            }
                        }
                    }
                })
            }
        }

    }

   /* private fun addCart(model: Product) {
            val requestModel = RequestModel()
            if (model.type == "variable") {
                requestModel.pro_id = model.variations!![0]
            } else {
                requestModel.pro_id = model.id
            }
            requestModel.quantity = 1
            (activity as AppBaseActivity).addItemToCart(requestModel, onApiSuccess = {
                activity!!.fetchAndStoreCartData()
            })

    }*/

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_view_all, menu)
        val menuWishItem = menu.findItem(R.id.action_cart)
        menuWishItem.isVisible = true
        menuCart = menuWishItem.actionView
        menuWishItem.actionView.setOnClickListener {
              //  launchActivity<MyCartActivity>()
        }
        //setCartCount()
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*fun setCartCount() {
        val count = getCartCount()
        if (menuCart != null) {
            menuCart?.ivCart?.changeBackgroundImageTint(getTextTitleColor())
            menuCart?.tvNotificationCount?.changeTint(getTextTitleColor())
            menuCart!!.tvNotificationCount.text = count
            menuCart!!.tvNotificationCount.changeAccentColor()
            if (count.checkIsEmpty() || count == "0") {
                menuCart!!.tvNotificationCount.hide()
            } else {
                menuCart!!.tvNotificationCount.show()
            }
        }

    }*/

    private fun loadCategory() {
        if (isNetworkAvailable()) {
            //listAllCategoryProduct()
            //Toast.makeText(activity,"load",Toast.LENGTH_LONG).show()
            MyApp.getWooApi().getProductByCategory(countLoadMore,8,mCategoryId).enqueue(object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                }
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {

                    if (activity == null) return

                    if (countLoadMore == 1) {
                        mProductAdapter.clearItems()
                    }
                    if (response.body().isNullOrEmpty()) {
                        isLastPage = true
                    }else{
                        mIsLoading = false
                        mProductAdapter.addMoreItems(response.body()!!)
                    }
                    if (mProductAdapter.itemCount == 0) {
                        rvNewestProduct.hide()
                    } else {
                        rvNewestProduct.show()
                    }
                }
            })


        }
    }

    private fun loadSubCategory() {
        if (isNetworkAvailable()) {
            //listAllCategory()

            MyApp.getWooApi().getCheldOfCategory(1,8,mCategoryId).enqueue(object : Callback<List<Category>> {
                override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                }
                override fun onResponse(call: Call<List<Category>>, response: Response<List<Category>>) {

                    if (activity == null) return
                    if (subCategoryCountLoadMore == 1) {
                        mSubCategoryAdapter.clearItems()
                    }
                    if (response.body().isNullOrEmpty()) {
                        mIsLastPage = true
                    }
                    mIsLoading = false
                    mSubCategoryAdapter.addMoreItems(response.body()!!)
                    if (mSubCategoryAdapter.itemCount == 0) {
                        rvCategory.hide()
                    } else {
                        rvCategory.show()
                    }

                }
            })


        }
    }

    private fun loadData() {
        if (isNetworkAvailable()) {

            val call = object : Callback<List<Product>> {
                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                }
                override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                    if (activity == null) return
                    if (countLoadMore == 1) {
                        mProductAdapter.clearItems()
                    }
                    mIsLoading = false
                    mProductAdapter.addMoreItems(response.body()!!)
                }
            }

            when (mId) {
                FEATURED -> MyApp.getWooApi().getFeaturedProduct(countLoadMore,8).enqueue(call)
                NEWEST -> MyApp.getWooApi().getNewProducts(countLoadMore,8).enqueue(call)
                SALE -> MyApp.getWooApi().getOnSaleProduct(countLoadMore,8).enqueue(call)
                POPULAR -> MyApp.getWooApi().getPopularProducts(countLoadMore,8).enqueue(call)
                RANDOM -> MyApp.getWooApi().getRandomProduct(countLoadMore,8).enqueue(call)
                MOSTRATE -> MyApp.getWooApi().getMostRateProduct(countLoadMore,8).enqueue(call)
            }

        }
    }


}
