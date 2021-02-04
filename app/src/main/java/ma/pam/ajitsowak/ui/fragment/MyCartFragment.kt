package ma.pam.ajitsowak.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.ui.activity.OrderSummaryActivity
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.model.ShippingZoneMethod
import ma.pam.ajitsowak.room.CartItem
import ma.pam.ajitsowak.ui.activity.CouponActivity
import ma.pam.ajitsowak.ui.activity.DashBoardActivity
import ma.pam.ajitsowak.ui.activity.EditProfileActivity
import ma.pam.ajitsowak.ui.activity.WishlistActivity
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.KeyIntent.DISCOUNT
import ma.pam.ajitsowak.utils.Constants.KeyIntent.PRICE
import ma.pam.ajitsowak.utils.Constants.KeyIntent.PRODUCTDATA
import ma.pam.ajitsowak.utils.Constants.KeyIntent.SHIPPING
import ma.pam.ajitsowak.utils.Constants.KeyIntent.SHIPPINGDATA
import ma.pam.ajitsowak.utils.Constants.KeyIntent.SUBTOTAL
import ma.pam.ajitsowak.woolib.models.Coupon
import ma.pam.ajitsowak.woolib.models.LineItem
import ma.pam.ajitsowak.woolib.models.ShippingAddress


@SuppressLint("SetTextI18n")
class MyCartFragment : Fragment() {

    private var mCoupons: Coupon? = null
    private var mTotalCount = 0.0
    private var mSubTotal = 0.0
    private var mTotalDiscount = "0"
    private var mShippingCost = "0"
    private var cartItemId = ""
    private var isRemoveCoupons = true
    private var mOrderItems: ArrayList<LineItem>? = ArrayList()
    private var shippingMethods = ArrayList<ShippingZoneMethod>()
    private var shippingMethodsAvailble = ArrayList<ShippingZoneMethod>()
    private var shipping: ShippingAddress? = null
    private var selectedMethod = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart, container, false)
    }

    private val mCartAdapter = BaseAdapter<CartItem>(R.layout.item_cart, onBind = { view, model, position ->

        val ivProduct = view.findViewById<ImageView>(R.id.ivProduct)
        val tvProductName = view.findViewById<TextView>(R.id.tvProductName)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvOriginalPrice = view.findViewById<TextView>(R.id.tvOriginalPrice)
        val qty_spinner = view.findViewById<TextView>(R.id.qty_spinner)
        val delete_layout = view.findViewById<CardView>(R.id.delete_layout)

        val ivMinus = view.findViewById<ImageButton>(R.id.ivMinus)
        val ivAdd = view.findViewById<ImageButton>(R.id.ivAdd)
        val front_layout = view.findViewById<RelativeLayout>(R.id.front_layout)



        tvProductName.text = model.productName
        tvOriginalPrice.applyStrike()


        if (model.productImage.isNotEmpty()) {
            ivProduct.loadImageFromUrl(model.productImage)
        }

        tvPrice.text = (model.price.toFloat() * model.quantity.toFloat()).toString().currencyFormat()

        qty_spinner.text = model.quantity.toString()

        delete_layout.setOnClickListener {
            removeCartItem(model)
        }

        ivMinus.setOnClickListener { minusClick(model, position) }

        ivAdd.setOnClickListener { addClick(model, position) }

        front_layout.setOnClickListener {
            // TODO: 24/01/2021 reper this
           /* startActivity(Intent(activity, ProductDetailActivity1::class.java).apply {
                putExtra(DATA, model.)
            })*/
        }
            //tvProductName.changeTextPrimaryColor()
            //qty_spinner.changeTextPrimaryColor()

           // ivMinus.backgroundTintList=ColorStateList.valueOf(Color.parseColor(getPrimaryColor()))
           // ivAdd.backgroundTintList=ColorStateList.valueOf(Color.parseColor(getPrimaryColor()))
           // tvPrice.changeTextPrimaryColor()
           // tvOriginalPrice.changeTextSecondaryColor()
           // delete_layout.setCardBackgroundColor(Color.parseColor(getButtonColor()))
        })

    private val mShippingMethodAdapter = BaseAdapter<ShippingZoneMethod>(R.layout.item_shipping_method, onBind = { view, model, position ->
        val shippingMethod = view.findViewById<TextView>(R.id.shippingMethod)
        val imgDone = view.findViewById<ImageView>(R.id.imgDone)

            if (model.method_id == "free_shipping" || model.cost.value == "0" || model.cost.value.isEmpty()) {

                shippingMethod.text = model.method_title
            } else {
                shippingMethod.text = model.method_title+ ": " + model.cost.value.currencyFormat()
            }
            if (selectedMethod == position) {
                imgDone.setImageResource(R.drawable.ic_baseline_done_24)
            } else {
                imgDone.setImageResource(R.drawable.bg_ractangal)
            }
           // shippingMethod.changeTextPrimaryColor()
        })

    private fun addClick(model: CartItem, position: Int) {
        val qty = model.quantity
        mCartAdapter.items[position].quantity = qty.plus(1)
        mCartAdapter.notifyItemChanged(position)
        removeCoupon()
        updateCartItem(mCartAdapter.items[position])
    }

    private fun minusClick(model: CartItem, position: Int) {
        val qty = model.quantity
        if (qty > 1) {
            mCartAdapter.items[position].quantity = qty.minus(1)
            mCartAdapter.notifyItemChanged(position)
            removeCoupon()
            updateCartItem(mCartAdapter.items[position])
        }
    }

    lateinit var tvShipping:TextView
    lateinit var llShippingAmount:LinearLayout
    lateinit var tvTotalCartAmount:TextView

    lateinit var lay_button:LinearLayout
    lateinit var rvCart:RecyclerView
    lateinit var llShipping:LinearLayout
    lateinit var llNoItems:LinearLayout
    lateinit var nsvCart: NestedScrollView


    lateinit var tvTotal:TextView
    lateinit var tvDiscount:TextView
    lateinit var tvEditCoupon:TextView
    lateinit var tvAddress:TextView
    lateinit var tvFreeShipping:TextView
    lateinit var txtApplyCouponCode:TextView
    lateinit var txtDiscountlbl:TextView


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        //changeColor()

        val tvContinue = view.findViewById<Button>(R.id.tvContinue)
        val tvChange = view.findViewById<TextView>(R.id.tvChange)
        val rvShippingMethod = view.findViewById<RecyclerView>(R.id.rvShippingMethod)
        val btnShopNow = view.findViewById<Button>(R.id.btnShopNow)
        val rlCoupon = view.findViewById<RelativeLayout>(R.id.rlCoupon)
        rvCart = view.findViewById(R.id.rvCart)
        tvShipping = view.findViewById(R.id.tvShipping)
        llShippingAmount = view.findViewById(R.id.llShippingAmount)
        tvTotalCartAmount = view.findViewById(R.id.tvTotalCartAmount)
        lay_button = view.findViewById(R.id.lay_button)
        llShipping = view.findViewById(R.id.llShipping)
        llNoItems = view.findViewById(R.id.llNoItems)
        nsvCart = view.findViewById(R.id.nsvCart)

        tvTotal = view.findViewById(R.id.tvTotal)
        tvDiscount = view.findViewById(R.id.tvDiscount)
        tvEditCoupon = view.findViewById(R.id.tvEditCoupon)
        tvAddress = view.findViewById(R.id.tvAddress)
        tvFreeShipping = view.findViewById(R.id.tvFreeShipping)
        txtApplyCouponCode = view.findViewById(R.id.txtApplyCouponCode)
        txtDiscountlbl = view.findViewById(R.id.txtDiscountlbl)


        tvContinue.setOnClickListener {
            if (!shipping?.country.isNullOrEmpty()) {
                var selectedShippingMethod: ShippingZoneMethod? = null
                val mAmount = mTotalCount + mShippingCost.toDouble()
                if (shippingMethodsAvailble.isNotEmpty()) {
                    if (selectedMethod == -1) {
                        Toast.makeText(activity,"Select Shipping Method",Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    selectedShippingMethod = shippingMethodsAvailble[selectedMethod]
                }

                val intent  = Intent(activity, OrderSummaryActivity::class.java).apply {
                    if(mCoupons!=null){
                        putExtra(Constants.KeyIntent.COUPON_CODE, mCoupons)
                    }
                    putExtra(PRICE, mAmount.toString())
                    putExtra(PRODUCTDATA, mOrderItems)
                    putExtra(SHIPPINGDATA, selectedShippingMethod)
                    putExtra(SUBTOTAL, mSubTotal.toString())
                    putExtra(DISCOUNT, mTotalDiscount)
                    putExtra(SHIPPING, mShippingCost)
                }
                startActivity(intent)

            } else {
                Toast.makeText(context, "You do not provided shipping address.", Toast.LENGTH_SHORT).show()
            }
        }

        tvChange.setOnClickListener {
            startActivityForResult(Intent(activity, EditProfileActivity::class.java),Constants.RequestCode.EDIT_PROFILE)
        }
        rvCart.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvCart.adapter = mCartAdapter

        rvShippingMethod.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvShippingMethod.adapter = mShippingMethodAdapter

        mShippingMethodAdapter.onItemClick = { pos, _, model ->
            onShippingMethodChanged(pos, model)
        }
        btnShopNow.setOnClickListener {
            startActivity(Intent(activity,DashBoardActivity::class.java))
        }

        if (getSharedPrefInstance().getBooleanValue(Constants.SharedPref.ENABLE_COUPONS, true)) {
            rlCoupon.visibility = View.VISIBLE
        } else {
            rlCoupon.visibility = View.GONE
        }

        invalidateCartLayout()
    }

    private fun onShippingMethodChanged(pos: Int, model: ShippingZoneMethod) {

        activity?.runOnUiThread {

            selectedMethod = pos
            mShippingMethodAdapter.notifyDataSetChanged()
            if (model.id == "free_shipping") {
                tvShipping.text = "Free"
                mShippingCost = "0"
               // tvShipping.setTextColor(activity!!.color(R.color.green))
                llShippingAmount.show()
                Log.d("mTotalCount", mTotalCount.toString())
            } else {
                if (model.cost.value.isNullOrEmpty()) {
                    model.cost.value = "0"
                }
                mShippingCost = model.cost.value
                llShippingAmount.show()
               // tvShipping.changeTextSecondaryColor()
                tvShipping.text = mShippingCost.currencyFormat()
            }
            tvTotalCartAmount.text = (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_cart, menu)
        val positionOfMenuItem = 0
        val item = menu.getItem(positionOfMenuItem)
        val s = SpannableString(getString(R.string.lbl_wish_list))
        item.title = s
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_wishlist -> {
                startActivity(Intent(activity, WishlistActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun invalidatePaymentLayout(b: Boolean) {
        if (activity != null) {
            if (!b) {
                lay_button.hide()
                rvCart.hide()
                // rvShipping.hide()
                llShipping.hide()
            } else {
                lay_button.show()
                llShipping.show()
                rvCart.show()
                // rvShipping.show()
            }
        }
    }



    private fun removeMultipleCartItem() {
        // TODO: 24/01/2021 remove all item
       /* val requestModel = CartRequestModel()
        requestModel.multpleId = cartItemId
        (activity as AppBaseActivity).removeMultipleCartItem(requestModel, onApiSuccess = {
            activity!!.finish()
        })*/
    }

    private fun removeCartItem(model: CartItem) {
        // TODO: 24/01/2021
        getRoom().Dao().DeleteCart(model)
        invalidateCartLayout()
    }

    fun invalidateCartLayout() {

        val carts = getRoom().Dao().cartList

        getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, carts.size)
                
        if (carts.size == 0) {
            invalidatePaymentLayout(false)
            llNoItems.show()
            lay_button.hide()
            llShipping.hide()
            nsvCart.hide()
        } else {
            llNoItems.hide()
            lay_button.show()
            llShipping.show()
            nsvCart.show()
            if (activity != null) {
                llNoItems.hide()
                cartItemId = ""
                mTotalCount = 0.0
                mOrderItems?.clear()
                for (i in 0 until carts.size) {
                    val itemData = LineItem()
                    itemData.product_id = carts[i].productId
                    itemData.quantity = carts[i].quantity
                    itemData.image = carts[i].productImage
                    mOrderItems?.add(itemData)

                    mTotalCount += try {
                        when {
                            carts[i].sale_price.isNotEmpty() -> {
                                carts[i].sale_price.toFloat().toInt() * carts[i].quantity
                            }
                            carts[i].regular_price.isNotEmpty() -> {
                                carts[i].regular_price.toFloat().toInt() * carts[i].quantity
                            }
                            else -> {
                                carts[i].price.toFloat().toInt() * carts[i].quantity
                            }
                        }
                    } catch (e: Exception) {
                        carts[i].price.toFloat().toInt() * carts[i].quantity
                    }
                    cartItemId = if (cartItemId.isNotEmpty()) {
                        cartItemId + "," + itemData.product_id.toString()
                    } else {
                        itemData.product_id.toString()
                    }
                }
                        mSubTotal = mTotalCount
                        tvTotal.text = mTotalCount.toString().currencyFormat()
                        if (isRemoveCoupons) {
                            tvDiscount.text = "0".currencyFormat()
                            tvTotalCartAmount.text = (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()

                            tvEditCoupon.text = getString(R.string.lbl_apply)
                            tvEditCoupon.setOnClickListener {

                                startActivityForResult(Intent(activity,CouponActivity::class.java),Constants.RequestCode.COUPON_CODE)
                            }
                        } else {
                            applyCouponCode()
                        }
                        invalidatePaymentLayout(true)
                        mCartAdapter.clearItems()
                        mCartAdapter.addItems(carts)

                        fetchShippingMethods()

                    }
                }

// TODO: 28/01/2021  on error
               /* getSharedPrefInstance().setValue(Constants.SharedPref.KEY_CART_COUNT, 0)
                llNoItems.show()
                lay_button.hide()
                llShipping.hide()
                nsvCart.hide()*/


    }


    private fun fetchShippingMethods() {
        if (shipping == null) {
            shipping = getShippingList() // TODO: 26/01/2021 cliant choping adress
        }

        if (!shipping?.country.isNullOrEmpty()) {
            shipping?.apply {
                val sap=","
                val adresse = "$address1$sap$address2$sap$city $postcode$sap$country"
                tvAddress.text = adresse
            }

            // TODO: 28/01/2021 get method shiping  by info -> get id cantry -> get post code

            // TODO: 28/01/2021 tomprary insert manualy

            val sh = ShippingZoneMethod().apply {
                id="free_shipping"
                title="Free shipping"
                order= 2
                isEnabled= true
                method_id="free_shipping"
                method_title = "Free shipping"
            }
            shippingMethods.clear()
            shippingMethods.add(sh)
            invalidateShippingMethods()

        } else {
            tvAddress.text = "You do not provided shipping address."
            llShippingAmount.hide()
           // tvFreeShipping.show()
        }
    }

    private fun invalidateShippingMethods() {
        shippingMethodsAvailble.clear()
        mShippingMethodAdapter.clearItems()
        if (shippingMethods.isNullOrEmpty()) {
            llShippingAmount.hide()
            tvFreeShipping.show()
        } else {
            llShippingAmount.hide()
            tvFreeShipping.hide()
            shippingMethods.forEachIndexed { _, attr ->
                if (attr.isEnabled) {
                    if (attr.method_id == "free_shipping") {
                        if (isMethodAvailable(attr)) {
                            shippingMethodsAvailble.add(attr)
                        }
                    } else {
                        shippingMethodsAvailble.add(attr)
                    }
                }
            }
            mShippingMethodAdapter.addItems(shippingMethodsAvailble)
            if (shippingMethodsAvailble.isEmpty()) {
                tvFreeShipping.show()
            } else {
                onShippingMethodChanged(0, shippingMethodsAvailble[0])
            }
        }
    }


    private fun isMethodAvailable(method: ShippingZoneMethod): Boolean {
        return true

      /*  when (method.requires) {
            "either" -> minAmount(method) || coupan()
            "both" -> minAmount(method) && coupan()
            "min_amount" -> minAmount(method)
            "coupon" -> coupan()
            else -> true
        }*/

    }

    private fun minAmount(method: ShippingZoneMethod): Boolean {
        return mTotalCount >= 0 //  method.minAmount.toDouble()

        // mSubTotal >= method.minAmount.toDouble()

    }

    private fun coupan(): Boolean {
        return !isRemoveCoupons && mCoupons != null && mCoupons?.free_shipping.toBoolean()
    }

    private fun updateCartItem(model: CartItem) {
        getRoom().Dao().UpdateCart(model)
        invalidateCartLayout()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.COUPON_CODE -> {
                    mCoupons = data!!.getSerializableExtra("couponData") as Coupon
                    isRemoveCoupons = false
                    applyCouponCode()
                }
                Constants.RequestCode.ORDER_SUMMARY -> {
                    removeMultipleCartItem()
                }
                Constants.RequestCode.EDIT_PROFILE -> {
                    shipping = null
                    fetchShippingMethods()
                }
                Constants.RequestCode.WISHLIST -> {
                    invalidateCartLayout()
                }
                else -> {
                    activity?.finish()
                }
            }
        }
    }

    /**
     * Apply coupon code
     *
     */
    private fun applyCouponCode() {
        if (mCoupons != null) {
            mTotalDiscount = "0"
            if (mCoupons!!.minimum_amount!!.toFloat() > 0.0) {
                if (mTotalCount < mCoupons!!.minimum_amount!!.toFloat()) {
                    txtApplyCouponCode.text = getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.minimum_amount!!.currencyFormat() + getString(
                            R.string.lbl_coupon_is_valid_only_orders_of1
                        )
                    return
                } else if (mCoupons!!.maximum_amount!!.toFloat() > 0.0) {
                    if (mTotalCount > mCoupons!!.maximum_amount!!.toFloat()) {
                        txtApplyCouponCode.text = getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.maximum_amount!!.currencyFormat() + " and below."
                        return
                    }
                }
            } else if (mCoupons!!.maximum_amount!!.toFloat() > 0.0) {
                if (mTotalCount > mCoupons!!.maximum_amount!!.toFloat()) {
                    txtApplyCouponCode.text = getString(R.string.lbl_coupon_is_valid_only_orders_of) + mCoupons!!.maximum_amount!!.currencyFormat() + " and below."
                    return
                }
            } else if (mCoupons?.discount_type == "fixed_cart") {
                if (mTotalCount < mCoupons!!.amount!!.toFloat()) {
                    txtApplyCouponCode.text = "Coupon is valid only order of " + (mCoupons!!.amount!!.toFloat() + 55) + " and above. Try other coupon."
                    return
                }
            } else if (mCoupons?.discount_type == "fixed_product") {
                var isValidCoupon = true
                for (i in 0 until mCartAdapter.getModel().size) {
                    if (mCartAdapter.getModel()[i].sale_price.isNotEmpty()) {
                        if (mCartAdapter.getModel()[i].sale_price.toFloat() < mCoupons!!.amount!!.toFloat()) {
                            isValidCoupon = false
                            break
                        }
                    } else {
                        if (mCartAdapter.getModel()[i].price.isNotEmpty()) {
                            if (mCartAdapter.getModel()[i].price.toFloat() < mCoupons!!.amount!!.toFloat()) {
                                isValidCoupon = false
                            }
                        }
                    }
                }
                if (!isValidCoupon) {
                    txtApplyCouponCode.text = "Coupon is valid only if all product price have " + (mCoupons!!.amount!!.toFloat() + 55) + " and above. Try other coupon."
                    return
                }
            }
            when (mCoupons?.discount_type) {
                "percent" -> {
                    mTotalDiscount = ((mTotalCount.toFloat() * mCoupons!!.amount!!.toFloat()) / 100).toString()
                    txtDiscountlbl.text = getString(R.string.lbl_discount) + " (" + mCoupons!!.amount + getString(R.string.lbl_off) + ")"
                }
                "fixed_cart" -> {
                    mTotalDiscount = mCoupons!!.amount!!
                    txtDiscountlbl.text = getString(R.string.lbl_discount) + " (" + getString(R.string.lbl_flat) + mCoupons!!.amount!!.currencyFormat() + getString(
                            R.string.lbl_off
                        ) + ")"
                }
                "fixed_product" -> {
                    val finalAmout = mCoupons!!.amount!!.split(".")
                    mTotalDiscount = (finalAmout[0].toInt() * (mOrderItems!!.size)).toString()
                    txtDiscountlbl.text = getString(R.string.lbl_discount)
                }
                else -> {
                    mTotalDiscount = mCoupons!!.amount!!
                    txtDiscountlbl.text = getString(R.string.lbl_discount) + " (" + getString(R.string.lbl_flat) + mCoupons!!.amount!!.currencyFormat() + getString(
                            R.string.lbl_off1
                        ) + ")"
                }
            }
            if (mTotalDiscount.toDouble() == 0.0) {
                tvDiscount.text = mTotalDiscount.currencyFormat()
            } else {
                tvDiscount.text = "-" + mTotalDiscount.currencyFormat()
            }
            mTotalCount = mTotalCount.minus(mTotalDiscount.toFloat())

            tvTotalCartAmount.text = (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
            txtApplyCouponCode.text = getString(R.string.lbl_applied_coupon) + mCoupons!!.code!!.toUpperCase()
            tvEditCoupon.text = getString(R.string.lbl_remove)

            tvEditCoupon.setOnClickListener {
                removeCoupon()
            }
            invalidateShippingMethods()
        }
    }

    private fun removeCoupon() {
        isRemoveCoupons = true
        mCoupons = null
        txtDiscountlbl.text = getString(R.string.lbl_discount)
        txtApplyCouponCode.text = getString(R.string.lbl_coupon_code)
        mTotalCount += mTotalDiscount.toFloat()
        tvEditCoupon.text = getString(R.string.lbl_apply)
        tvDiscount.text = "0".currencyFormat()
        tvTotalCartAmount.text = (mTotalCount.toInt() + mShippingCost.toInt()).toString().currencyFormat()
        tvEditCoupon.setOnClickListener {
            startActivityForResult(Intent(activity,CouponActivity::class.java),Constants.RequestCode.COUPON_CODE)
        }
        invalidateShippingMethods()
    }

   /* private fun changeColor()
    {
        lblNoItems.changeTextPrimaryColor()
        btnShopNow.changeBackgroundTint(getButtonColor())
        txtApplyCouponCode.changeTextSecondaryColor()
        tvEditCoupon.changePrimaryColor()
        lblShipping.changeTextPrimaryColor()
        tvChange.changePrimaryColor()
        tvAddress.changeTextSecondaryColor()
        tvFreeShipping.changeTextSecondaryColor()
        lblSubTotal.changeTextSecondaryColor()
        tvTotal.changeTextSecondaryColor()
        txtDiscountlbl.changeTextSecondaryColor()
        tvDiscount.changeTextSecondaryColor()
        txtShippinglbl.changeTextSecondaryColor()
        tvShipping.changeTextSecondaryColor()
        lblTotalAmount.changeTextPrimaryColor()
        tvTotalCartAmount.changeTextPrimaryColor()
        tvContinue.changeBackgroundTint(getButtonColor())
        rlMain.changeBackgroundColor()
    }
*/
}
