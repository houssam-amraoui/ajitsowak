package ma.pam.ajitsowak.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.button.MaterialButton

import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.adapter.ProductImageAdapter
import ma.pam.ajitsowak.adapter.SpinnerAdapter
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.room.CartItem
import ma.pam.ajitsowak.room.FavModel
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.KeyIntent.DATA
import ma.pam.ajitsowak.utils.Constants.KeyIntent.PRODUCT_ID
import ma.pam.ajitsowak.utils.Constants.viewAllCode.CATEGORY
import ma.pam.ajitsowak.woolib.models.Category
import ma.pam.ajitsowak.woolib.models.Product

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

@SuppressLint("SetTextI18n")
class ProductDetailActivity1 : mAppCompatActivity() {
    private var mProductId = 0
    private val mImages = ArrayList<String>()
    private var mMenuCart: View? = null
    private var mIsInWishList = false
    private var isAddedToCart: Boolean = false
    private var mIsExternalProduct = false
    private var mExternalURL: String = ""
    private var mAttributeAdapter: BaseAdapter<String>? = null
    private var mYearAdapter: ArrayAdapter<String>? = null
    private var image: String = ""
    private var count: Int = 0


    lateinit var toolbar:Toolbar
    lateinit var rvLike:RecyclerView
    lateinit var rvCategory:RecyclerView
    lateinit var btnAddCard:MaterialButton
    lateinit var tvItemProductOriginalPrice:TextView
    lateinit var llReviews:LinearLayout
    lateinit var toolbar_layout: CollapsingToolbarLayout
    lateinit var app_bar:AppBarLayout
    lateinit var ivFavourite:ImageView
    lateinit var tvName:TextView
    lateinit var scrollView: NestedScrollView
    lateinit var tvItemProductRating:RatingBar
    lateinit var tvTags:TextView
    lateinit var btnOutOfStock:MaterialButton

    lateinit var product:Product


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail1)

        toolbar = findViewById(R.id.toolbar)
        rvLike = findViewById(R.id.rvLike)
        rvCategory = findViewById(R.id.rvCategory)
        btnAddCard = findViewById(R.id.btnAddCard)
        tvItemProductOriginalPrice = findViewById(R.id.tvItemProductOriginalPrice)
        llReviews = findViewById(R.id.llReviews)
        toolbar_layout = findViewById(R.id.toolbar_layout)
        app_bar = findViewById(R.id.app_bar)
        ivFavourite = findViewById(R.id.ivFavourite)
        tvName = findViewById(R.id.tvName)
        scrollView = findViewById(R.id.scrollView)
        tvItemProductRating = findViewById(R.id.tvItemProductRating)
        tvTags = findViewById(R.id.tvTags)
        btnOutOfStock = findViewById(R.id.btnOutOfStock)
        setDetailToolbar(toolbar)
        //changeColor()

        if (intent?.extras?.containsKey(PRODUCT_ID) == false) {
            Toast.makeText(this,getString(R.string.error_something_went_wrong),Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        mProductId = intent?.getIntExtra(PRODUCT_ID, 0)!!


        rvLike.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvLike.adapter = mProductAdapter

        rvCategory.apply {
            layoutManager = GridLayoutManager(this@ProductDetailActivity1, 2)
            setHasFixedSize(true)
            adapter = mCategoryAdapter
        }
        scrollView.visibility = View.GONE

        if (intent?.extras?.containsKey(DATA) == true) {
            product = intent?.extras?.getSerializable(DATA) as Product
            getProductDetail(product)
            initListener()
        }
        else{
            if (isNetworkAvailable()) {
                showProgress(true)
                getWooApi().getProductDetail(mProductId).enqueue(object : Callback<Product> {
                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        showProgress(false)
                    }
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        if (response.body() != null) {
                            product = response.body()!!
                            getProductDetail(product)
                            initListener()
                        }
                        showProgress(false)
                    }
                })
            }
        }
    }

    fun initListener(){
        btnAddCard.setOnClickListener {
            if (isAddedToCart)
                removeCartItem(product)
            else
                addToCart(product)
        }
        llReviews.setOnClickListener() {
            /* launchActivity<ReviewsActivity> {
                 putExtra(PRODUCT_ID, intent?.getIntExtra(PRODUCT_ID, 0)!!)
             }*/
        }
        toolbar_layout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar)
        // toolbar_layout.setContentScrimColor(Color.parseColor(getPrimaryColor()))
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) - app_bar.totalScrollRange == 0) {
                toolbar_layout.title = tvName.text
            } else {
                toolbar_layout.title = ""
            }
        })
        ivFavourite.setOnClickListener {
            onFavouriteClick()
        }
    }

    private fun removeCartItem(product: Product) {
        val isItemRemoverd = getRoom().Dao().DeleteCart(CartItem(
                productId = product.id,
                productImage = product.images.first().src!!,
                quantity = 1,
                price = product.price,
                regular_price = product.regular_price,
                sale_price = product.sale_price

        ))
        Toast.makeText(this,isItemRemoverd.toString(),Toast.LENGTH_SHORT).show()
        if (isItemRemoverd != -1) {
            btnAddCard.text = getString(R.string.lbl_add_to_cart)
            isAddedToCart = false
            if (count != 0)
                getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, count - 1)
            setCartCount()
        }
    }

    private fun addToCart(product: Product) {
        val isItemAdded = getRoom().Dao().addToCart(CartItem(
                productId = product.id,
                productImage = product.images.first().src!!,
                quantity = 1,
                price = product.price,
                regular_price = product.regular_price,
                sale_price = product.sale_price

        ))
        if (isItemAdded != (-1).toLong()) {
            getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, count + 1)
            setCartCount()
            btnAddCard.text = getString(R.string.lbl_remove_cart)
            isAddedToCart = true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        val menuWishItem: MenuItem = menu!!.findItem(R.id.action_cart)
        val menuSearchItem: MenuItem = menu.findItem(R.id.action_search)
        menuWishItem.isVisible = true
        menuSearchItem.isVisible = false
        menuWishItem.actionView.findViewById<ImageView>(R.id.ivCart).setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.SRC_IN)
        mMenuCart = menuWishItem.actionView
        menuWishItem.actionView.setOnClickListener {
            startActivity(Intent(this, MyCartActivity::class.java))
        }



        setCartCount()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        setCartCount()
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


    private fun getProductDetail(product: Product) {
        scrollView.visibility = View.VISIBLE
        viewVariableImage(product)
        tvItemProductOriginalPrice.applyStrike()

        // Other Information
        tvName.text = product.name
        toolbar_layout.title = product.name
        tvItemProductRating.rating = product.average_rating.toFloat()

        tvTags.text = product.description.getHtmlString().toString()

        if (product.stock_status == "instock") {
            btnOutOfStock.hide()
            btnAddCard.show()
        } else {
            btnOutOfStock.show()
            btnAddCard.hide()
        }

        // Additional information
        if (!product.productAttributes.isNullOrEmpty()) {
            for (att in product.productAttributes) {
                val vi = applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val v: View = vi.inflate(R.layout.view_attributes, null)
                val textView = v.findViewById<View>(R.id.txtAttName) as TextView
                //textView.changeTextSecondaryColor()
                textView.text = att.name + " : "
                val sizeList = ArrayList<String>()
                val sizes = att.options
                sizes?.forEachIndexed { _, s ->
                    sizeList.add(s.trim())
                }

                mAttributeAdapter = BaseAdapter(R.layout.item_attributes, onBind = { vv, item, position ->
                    if (item.isNotEmpty()) {
                        val attSize = vv.findViewById<TextView>(R.id.tvSize)
                        attSize.typeface = fontRegular()
                        // attSize.changeTextSecondaryColor()
                        if (sizeList.size - 1 == position) {
                            attSize.text = item
                        } else {
                            attSize.text = "$item ,"
                        }
                    }
                })

                mAttributeAdapter?.clearItems()
                mAttributeAdapter?.addItems(sizeList)

                val recycleView = v.findViewById<View>(R.id.rvAttributeView) as RecyclerView
                recycleView.layoutManager = LinearLayoutManager(this@ProductDetailActivity1, RecyclerView.HORIZONTAL, false)
                recycleView.adapter = mAttributeAdapter
                val llAttributeView:LinearLayout = findViewById(R.id.llAttributeView)
                llAttributeView.addView(v, 0, ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                )
            }
        }

        // Attribute Information

        val tvAvailability = findViewById<TextView>(R.id.tvAvailability)
        val llAttribute = findViewById<LinearLayout>(R.id.llAttribute)
        val upcomingSale = findViewById<LinearLayout>(R.id.upcomingSale)
        val spAttribute = findViewById<Spinner>(R.id.spAttribute)
        val groupItems = findViewById<LinearLayout>(R.id.groupItems)
        val extraProduct = findViewById<RecyclerView>(R.id.extraProduct)
        val banner_container = findViewById<LinearLayout>(R.id.banner_container)
        val tvAllReviews = findViewById<TextView>(R.id.tvAllReviews)
        val lbl_like = findViewById<TextView>(R.id.lbl_like)
        val lblCategory = findViewById<TextView>(R.id.lblCategory)

        if (product.type == "simple") {

            if (product.productAttributes.isNotEmpty()) {
                tvAvailability.text = product.productAttributes[0].name.toString()
            }
            llAttribute.hide()
            setPriceDetail(product)

        } else
            if (product.type == "variable") {
                llAttribute.show()
                if ((!product.productAttributes.isNullOrEmpty()) && product.productAttributes.isNotEmpty()) {

                    val sizeList = ArrayList<String>()
                    val mVariationsList = ArrayList<Int>()
                    val mVariations = product.variations

                    // TODO: 17/01/2021 need test
                    /*it.forEachIndexed { i, details ->
                                if (i > 0) {
                                    var option = ""
                                    it[i].attributes!!.forEach { attr ->
                                        option = if (option.isNotBlank()) {
                                            option + " - " + attr.optionsString.toString()
                                        } else {
                                            attr.optionsString.toString()
                                        }
                                    }
                                    if (details.onSale) {
                                        option = "$option [Sale]"
                                    }
                                    sizeList.add(option)
                                }
                           }*/

                    mVariations.forEachIndexed { _, s ->
                        mVariationsList.add(s)
                    }

                    mYearAdapter = SpinnerAdapter(this@ProductDetailActivity1,sizeList)
                    spAttribute.adapter = mYearAdapter

                    spAttribute.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                            if (mVariationsList[position] == product.id) {
                                setPriceDetail(product)
                                tvAvailability.text = product.productAttributes[0].name.toString()
                                mYearAdapter!!.notifyDataSetChanged()
                            }
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {}
                    }

                } else {
                    llAttribute.hide()
                }
            } else
                if (product.type == "grouped") {
                    llAttribute.visibility = View.GONE
                    upcomingSale.visibility = View.GONE
                    groupItems.visibility = View.VISIBLE

                    extraProduct.layoutManager = LinearLayoutManager(this@ProductDetailActivity1, RecyclerView.VERTICAL, false)
                    extraProduct.adapter = mGroupCartAdapter

                    mGroupCartAdapter.clearItems()

                    getWooApi().getProductIncludeId(product.grouped_products.toIntArray()).enqueue(object : Callback<List<Product>> {
                        override fun onFailure(call: Call<List<Product>>, t: Throwable) {}

                        override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                            for (pr in response.body()!!)
                                mGroupCartAdapter.addItem(pr)
                        }
                    })

                mGroupCartAdapter.notifyDataSetChanged()

            } else if (product.type == "external") {
                llAttribute.hide()
                setPriceDetail(product)
                mIsExternalProduct = true
                btnAddCard.show()
                btnAddCard.text = product.button_text
                mExternalURL = product.external_url.toString()
            } else {
                Toast.makeText(this@ProductDetailActivity1,getString(R.string.invalid_product),Toast.LENGTH_SHORT).show()
                finish()
            }

        // Purchasable -- sell enabled
        if (!product.isPurchasable) {
            if (mIsExternalProduct) {
                banner_container.show()
            } else {
                banner_container.hide()
            }
        } else {
            banner_container.show()
        }

        //Reviews
        if (product.isReviews_allowed) {
            // TODO: 06/02/2021   tvAllReviews.show() just for now
            tvAllReviews.hide()

            llReviews.show()
            tvAllReviews.setOnClickListener {
                // launchActivity<ReviewsActivity> { putExtra(PRODUCT_ID, intent?.getIntExtra(PRODUCT_ID, 0)!!) }
            }
        } else {
            llReviews.hide()
            tvAllReviews.hide()
        }

        // like data
        if (product.categories.isNullOrEmpty()) {
            lbl_like.hide()
            rvLike.hide()
        } else {
            lbl_like.show()
            rvLike.show()
            // TODO: 17/01/2021 add all category instead one and fix exclude currant
            if (isNetworkAvailable()) {
                getWooApi().getProductByCategory(1, 5, product.categories[0].id).enqueue(object : Callback<List<Product>> {
                    override fun onFailure(call: Call<List<Product>>, t: Throwable) {}
                    override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                        mProductAdapter.addItems(response.body()!!)
                    }
                });
            }


        }

        // Category data
        if (product.categories.isNullOrEmpty()) {
            lblCategory.hide()
            rvCategory.hide()
        } else {
            lblCategory.show()
            rvCategory.show()
            mCategoryAdapter.clearItems()
            mCategoryAdapter.addItems(product.categories)
        }

        // check cart & wish list

        // TODO: 17/01/2021 need reper

        when {
            getRoom().Dao().isCartAdded(product.id) -> {
                isAddedToCart = true
                btnAddCard.text = getString(R.string.lbl_remove_cart)
            }
            else -> {
                isAddedToCart = false
                btnAddCard.text = getString(R.string.lbl_add_to_cart)
            }
        }

        when {
            getRoom().Dao().isFavsAdded(product.id) -> {
                mIsInWishList = true
                changeFavIcon(R.drawable.ic_heart_fill)
            }else -> {
            mIsInWishList = false
            changeFavIcon(R.drawable.ic_heart, R.color.colorAccent)
        }
        }

    }

    private fun calculateDiscount(salePrice: String?, regularPrice: String?): Float {
        return (100f - (salePrice!!.toFloat() * 100f) / regularPrice!!.toFloat())
    }

    //get and show Images from froduct
    private fun viewVariableImage(its: Product) {
        val productViewPager: ViewPager = findViewById(R.id.productViewPager)
        mImages.clear()
        for (i in its.images.indices) {
            its.images.get(i).src?.let { it1 -> mImages.add(it1) }
        }
        val adapter1 = ProductImageAdapter(mImages)
        productViewPager.adapter = adapter1
        /*
        dots.attachViewPager(productViewPager)
        dots.setDotDrawable(R.drawable.bg_circle_primary, R.drawable.black_dot)*/
        adapter1.setListener(object : ProductImageAdapter.OnClickListener {
            override fun onClick(position: Int) {
                // TODO: 17/01/2021 need reper
               /* launchActivity<ZoomImageActivity> {
                    putExtra(DATA, its)
                }*/
            }
        })

    }

    private fun setPriceDetail(its: Product) {
        val tvPrice = findViewById<TextView>(R.id.tvPrice)
        val tvSaleLabel = findViewById<TextView>(R.id.tvSaleLabel)
        val upcomingSale = findViewById<LinearLayout>(R.id.upcomingSale)
        val tvSaleDiscount = findViewById<TextView>(R.id.tvSaleDiscount)
        val tvSaleOffer = findViewById<TextView>(R.id.tvSaleOffer)

        mProductId = its.id

        if (its.isOn_sale) {
            tvPrice.text = its.price.currencyFormat()
            tvSaleLabel.show()
            tvItemProductOriginalPrice.applyStrike()
            tvItemProductOriginalPrice.text = its.regular_price.currencyFormat()
            upcomingSale.visibility = View.GONE
            val discount = calculateDiscount(its.sale_price, its.regular_price)
            if (discount > 0.0) {
                tvSaleDiscount.visibility = View.VISIBLE
                tvSaleDiscount.text = String.format("%.2f", discount) + getString(R.string.lbl_off)
            }
            //onSaleOffer(its)

        } else {
            tvSaleDiscount.visibility = View.INVISIBLE
            tvItemProductOriginalPrice.text = ""
            tvPrice.text = its.regular_price.currencyFormat()
            tvSaleLabel.hide()
            showUpComingSale(its)
            tvSaleOffer.visibility = View.GONE
        }
    }

    // Show Upcoming sale details


    private fun showUpComingSale(its: Product) {
        val upcomingSale = findViewById<LinearLayout>(R.id.upcomingSale)
        val tvUpcomingSale = findViewById<TextView>(R.id.tvUpcomingSale)

        if (its.date_on_sale_from != null) {
            upcomingSale.visibility = View.VISIBLE
            tvUpcomingSale.text = getString(R.string.lbl_sale_start_from) + " " + its.date_on_sale_from + " " + getString(R.string.lbl_to) + " " + its.date_on_sale_to + ". " + getString(R.string.lbl_ge_amazing_discounts_on_the_products)
        } else {
            upcomingSale.visibility = View.GONE
        }
    }

    private fun onFavouriteClick() {
        if (mIsInWishList) {
            changeFavIcon(R.drawable.ic_heart,R.color.colorAccent)
            ivFavourite.isClickable = false

            val favModel = FavModel()
            favModel.idProduct = mProductId

            getRoom().Dao().deleteFav(favModel).apply {
                ivFavourite.isClickable = true
                mIsInWishList = false
            }
        } else {
            changeFavIcon(R.drawable.ic_heart_fill);
            ivFavourite.isClickable = false

            val favModel = FavModel()
            favModel.idProduct = mProductId

            getRoom().Dao().insertFav(favModel).apply {
                ivFavourite.isClickable = true
                mIsInWishList = true
            }
        }
    }

    private fun changeFavIcon(drawable: Int, iconTint: Int = R.color.colorPrimary) {
        ivFavourite.setImageResource(drawable)
        ivFavourite.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(this, iconTint), PorterDuff.Mode.SRC_IN)
    }

  /*  private fun changeColor() {
        tvAvailability.changeAccentColor()
        tvSaleDiscount.changeTextPrimaryColor()
        tvName.changeTextPrimaryColor()
        tvPrice.changeAccentColor()
        tvItemProductOriginalPrice.changeTextSecondaryColor()
        tvSaleOffer.changeAccentColor()
        lblProductInclude.changeTextPrimaryColor()
        lblAvailable.changeTextPrimaryColor()
        lblAdditionInformation.changeTextPrimaryColor()
        lblUpcomingSale.changeTextPrimaryColor()
        tvUpcomingSale.changeTextSecondaryColor()
        lblDescription.changeTextPrimaryColor()
        tvTags.changeTextSecondaryColor()
        lblCategory.changeTextPrimaryColor()
        lblCategory.changeTextPrimaryColor()
        lbl_like.changeTextPrimaryColor()
        tvAllReviews.changeTextPrimaryColor()
        btnAddCard.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getButtonColor()))
        htab_maincontent.changeBackgroundColor()
    }*/

  // Showing Special Price Label count down

  private fun onSaleOffer(its: Product) {

      val tvSaleOffer = findViewById<TextView>(R.id.tvSaleOffer)

      if (its.date_on_sale_from != null) {
          tvSaleOffer.visibility = View.VISIBLE
          val endTime = its.date_on_sale_to.toString() + " 23:59:59"
          val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
          try {
              val endDate: Date = dateFormat.parse(endTime)
              val currentDate = Date()
              val different: Long = endDate.time - currentDate.time
              object : CountDownTimer(different, 1000) {
                  override fun onTick(millisUntilFinished: Long) {
                      var differenta: Long = millisUntilFinished
                      val sec = (millisUntilFinished / 1000).toString()
                      val secondsInMilli: Long = 1000
                      val minutesInMilli = secondsInMilli * 60
                      val hoursInMilli = minutesInMilli * 60
                      val daysInMilli = hoursInMilli * 24

                      val elapsedDays: Long = differenta / daysInMilli
                      differenta %= daysInMilli

                      val elapsedHours: Long = differenta / hoursInMilli
                      differenta %= hoursInMilli

                      val elapsedMinutes: Long = differenta / minutesInMilli
                      differenta %= minutesInMilli

                      val elapsedSeconds: Long = differenta / secondsInMilli
                      if (elapsedDays > 0) {
                          tvSaleOffer.text = getString(R.string.lbl_special_price_ends_in_less_then) + " " + elapsedDays + getString(R.string.lbl_d) + " " +
                                  elapsedHours + getString(R.string.lbl_h) + " " + elapsedMinutes + getString(R.string.lbl_m) + " " + elapsedSeconds + getString(R.string.lbl_s)
                      } else {
                          tvSaleOffer.text = getString(R.string.lbl_special_price_ends_in_less_then) + " " + elapsedHours + getString(R.string.lbl_h) + " " +
                                  elapsedMinutes + getString(R.string.lbl_m) + " " + elapsedSeconds + getString(R.string.lbl_s)
                      }
                  }

                  override fun onFinish() {
                      tvSaleOffer.visibility = View.GONE
                  }
              }.start()
          } catch (e: ParseException) {
              e.printStackTrace()
          }

      } else {
          tvSaleOffer.visibility = View.GONE
      }
  }


    private val mGroupCartAdapter = BaseAdapter<Product>(R.layout.item_group, onBind = { view, model, _ ->

        val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tvOriginalPrice)
        val tvAdd = view.findViewById<TextView>(R.id.tvAdd)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)

        tvProductName.text = model.name
        //tvProductName.changeTextPrimaryColor()
        //tvAdd.changeBackgroundTint(getAccentColor())
        //tvPrice.changeAccentColor()
        //tvOriginalPrice.changeTextSecondaryColor()
        tvOriginalPrice.applyStrike()
        if (model.images[0].src!!.isNotEmpty()) {
            ivProduct.loadImageFromUrl(model.images[0].src!!)
        }
        if (model.isOn_sale) {
            tvPrice.text = model.sale_price.currencyFormat()
            tvOriginalPrice.applyStrike()
            tvOriginalPrice.text = model.regular_price.currencyFormat()
        } else {
            tvOriginalPrice.text = ""
            if (model.regular_price.equals("")) {
                tvPrice.text = model.price.currencyFormat()
            } else {
                tvPrice.text = model.regular_price.currencyFormat()
            }
        }
        tvAdd.setOnClickListener {
            addToCart(model)
        }
    })
    //product you may be like
    private val mProductAdapter = BaseAdapter<Product>(R.layout.item_home_dashboard1, onBind = { view, model, _ ->
        setProductItem1(view, model)
    })
    //category of product
    private val mCategoryAdapter = BaseAdapter<Category>(R.layout.item_category, onBind = { view, model, _ ->
        val tvCategoryName = view.findViewById<TextView>(R.id.tvCategoryName)
        tvCategoryName.text = model.name
        //tvCategoryName.changeTextSecondaryColor()
        view.setOnClickListener {
            startActivity(Intent(this@ProductDetailActivity1,ViewAllProductActivity::class.java).apply {
                putExtra(Constants.KeyIntent.TITLE, model.name)
                putExtra(Constants.KeyIntent.VIEWALLID, CATEGORY)
                putExtra(Constants.KeyIntent.KEYID, model.id) })
        }
    })

    private fun setProductItem1(view: View, model: Product, params: Boolean = false) {
        val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvDiscountPrice = view.findViewById<TextView>(R.id.tvDiscountPrice)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tvOriginalPrice)
        val tvAdd = view.findViewById<TextView>(R.id.tvAdd)
        val tvSaleLabel = view.findViewById<TextView>(R.id.tvSaleLabel)

        if (!params) {
            ivProduct.layoutParams = productLayoutParams()
        } else {
            ivProduct.layoutParams = productLayoutParamsForDealOffer()
        }

        if (model.images[0].src!!.isNotEmpty()) {
            ivProduct.loadImageFromUrl(model.images[0].src!!)
            image = model.images[0].src!!
        }

        val mName = model.name.split(",")

        tvProductName.text = mName[0]
        //  tvProductWeight.changeAccentColor()
        // tvProductName.changeTextPrimaryColor()
        //  tvOriginalPrice.changeTextSecondaryColor()
        // tvDiscountPrice.changeTextPrimaryColor()
        // tvAdd.changeBackgroundTint(getAccentColor())

        if (model.sale_price.isNotEmpty()) {
            tvSaleLabel.visibility = View.VISIBLE
            tvDiscountPrice.text = model.sale_price.currencyFormat()
            tvOriginalPrice.applyStrike()
            tvOriginalPrice.text = model.regular_price.currencyFormat()
            tvOriginalPrice.visibility = View.VISIBLE
        } else {
            tvSaleLabel.visibility = View.GONE
            tvOriginalPrice.visibility = View.VISIBLE
            if (model.regular_price.isEmpty()) {
                tvOriginalPrice.text = ""
                tvDiscountPrice.text = model.price.currencyFormat()
            } else {
                tvOriginalPrice.text = ""
                tvDiscountPrice.text = model.regular_price.currencyFormat()
            }
        }

        view.setOnClickListener {
            val intent = Intent(this@ProductDetailActivity1,ProductDetailActivity1::class.java)
            intent.putExtra(PRODUCT_ID, model.id)
            intent.putExtra(DATA,model)
            startActivity(intent)

        }
        tvAdd.setOnClickListener {
            addToCart(model)
        }
    }

}

