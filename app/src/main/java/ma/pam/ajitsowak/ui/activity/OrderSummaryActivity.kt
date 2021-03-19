package ma.pam.ajitsowak.ui.activity


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import ma.pam.ajitsowak.Config
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.model.ShippingZoneMethod
import ma.pam.ajitsowak.room.OrderItem
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.KeyIntent.DISCOUNT
import ma.pam.ajitsowak.utils.Constants.KeyIntent.SHIPPINGDATA
import ma.pam.ajitsowak.utils.Constants.KeyIntent.SUBTOTAL

import ma.pam.ajitsowak.utils.Constants.PAYMENT_METHOD.PAYMENT_METHOD_WEB
import ma.pam.ajitsowak.woolib.models.*

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat


class OrderSummaryActivity : AppCompatActivity() {

    private val TAG: String = OrderSummaryActivity::class.toString()
    private var isNativePayment = false
    private var shippingItems: ShippingZoneMethod? = null // method and price an pro pret
    private var mCoupons: Coupon? = null
    private var orderId: Int? = null

    lateinit var tvTotal:TextView
    lateinit var llDiscount:LinearLayout
    lateinit var tvDiscount:TextView
    lateinit var llShippingAmount:LinearLayout
    lateinit var tvShipping:TextView
    lateinit var tvTotalCartAmount:TextView
    lateinit var tvContinue:TextView

    lateinit var rbCard: RadioButton
    lateinit var rbRazorpay:RadioButton
    lateinit var rbCOD:RadioButton

    lateinit var paymentView:LinearLayout
    lateinit var tvUserAddress:TextView
    lateinit var tvBillingAddress:TextView
    lateinit var tvMethod:TextView
    lateinit var tvMethodName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_summary)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setDetailToolbar(toolbar)
        title = getString(R.string.order_summary)
       // mAppBarColor()
       // changeColor()
        tvTotal = findViewById(R.id.tvTotal)
        llDiscount = findViewById(R.id.llDiscount)
        tvDiscount = findViewById(R.id.tvDiscount)
        llShippingAmount = findViewById(R.id.llShippingAmount)
        tvShipping = findViewById(R.id.tvShipping)
        tvTotalCartAmount = findViewById(R.id.tvTotalCartAmount)
        tvContinue = findViewById(R.id.tvContinue)

        rbCard = findViewById(R.id.rbCard)
        rbRazorpay = findViewById(R.id.rbRazorpay)
        rbCOD = findViewById(R.id.rbCOD)

        paymentView = findViewById(R.id.paymentView)
        tvUserAddress = findViewById(R.id.tvUserAddress)
        tvBillingAddress = findViewById(R.id.tvBillingAddress)
        tvMethod = findViewById(R.id.tvMethod)
        tvMethodName = findViewById(R.id.tvMethodName)


         //Check payment method type
        isNativePayment = getSharedPrefInstance().getStringValue(Constants.SharedPref.PAYMENT_METHOD, PAYMENT_METHOD_WEB) != PAYMENT_METHOD_WEB

        shippingItems = intent.getSerializableExtra(SHIPPINGDATA) as ShippingZoneMethod?

        if( intent.getSerializableExtra(Constants.KeyIntent.COUPON_CODE)!=null)
            mCoupons = intent.getSerializableExtra(Constants.KeyIntent.COUPON_CODE) as Coupon?

        val subtotal = intent.getStringExtra(SUBTOTAL)
        val discount = intent.getStringExtra(DISCOUNT)

        tvTotal.text = subtotal?.currencyFormat()
        if (discount != null && discount != "0") {
            llDiscount.show()
            tvDiscount.text = "-" + discount.currencyFormat()
        }
        if (shippingItems != null) {
            llShippingAmount.show()
            if (shippingItems!!.method_id == "free_shipping") {
                tvShipping.text = "Free"
               // tvShipping.setTextColor(getColor(R.color.green))
            } else {
                if (shippingItems!!.settings.cost.value.isEmpty()) {
                    shippingItems!!.settings.cost.value = "0"
                }
                llShippingAmount.show()
               // tvShipping.changeTextSecondaryColor()
                tvShipping.text = shippingItems!!.settings.cost.value.currencyFormat()
            }
        }
        val price = intent.getStringExtra(Constants.KeyIntent.PRICE)
        val precision = DecimalFormat("0.00")
        tvTotalCartAmount.text = precision.format(price?.toDouble()).toString().currencyFormat()

        updateAddress()



        tvContinue.setOnClickListener {
            if (isNativePayment) {
                R.id.rbCard
                when {
                    rbCard.isChecked -> {
                      /*  launchActivity<StripePaymentActivity>(Constants.RequestCode.STRIPE_PAYMENT) {
                            putExtra(Constants.KeyIntent.COUPON_CODE, intent.getStringExtra(Constants.KeyIntent.COUPON_CODE))
                            putExtra(Constants.KeyIntent.PRICE, intent.getStringExtra(Constants.KeyIntent.PRICE))
                            putExtra(Constants.KeyIntent.PRODUCTDATA, intent.getSerializableExtra(Constants.KeyIntent.PRODUCTDATA))
                        }*/
                    }
                    rbRazorpay.isChecked -> {

                    }
                    rbCOD.isChecked -> {
                        val orderRequest = Order()
                        orderRequest.payment_method = "cod"
                        orderRequest.transaction_id = ""
                       // orderRequest.customer_id = getUserId().toInt()
                        //orderRequest.currency = getDefaultCurrencyFormate()
                        orderRequest.status = "pending"
                        orderRequest.set_paid = false
                        createOrderRequest(orderRequest)
                    }
                }
            } else {

                val orderRequest = Order()
                orderRequest.payment_method = "cod"
                orderRequest.transaction_id = ""
                //orderRequest.customer_id = getUserId().toInt()
                //orderRequest.currency = getDefaultCurrencyFormate()
                orderRequest.status = "pending"
                orderRequest.set_paid = false
                createOrderRequest(orderRequest)
            }
        }


        if (isNativePayment) {
            paymentView.visibility = View.VISIBLE
        } else {
            paymentView.visibility = View.GONE
        }
    }

    private fun updateAddress() {
        val shipping = getShippingList()
        tvUserAddress.text = shipping.firstName + " " + shipping.lastName + "\n" + shipping.getFullAddress(sap = "\n")
        val billing = getbillingList()
        tvBillingAddress.text = billing.firstName + " " + billing.lastName + "\n" + billing.getFullAddress(sap = "\n")
        if (shippingItems != null) {
            tvMethod.show()
            if (shippingItems!!.method_id == "free_shipping" || shippingItems!!.settings.cost.value == "0" || shippingItems!!.settings.cost.value.isEmpty()) {
                tvMethodName.text = shippingItems!!.method_title
            } else {
                tvMethodName.text = shippingItems!!.method_title + ": " + shippingItems!!.settings.cost.value.currencyFormat()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.RequestCode.ADD_ADDRESS -> {
                    updateAddress()
                }
                Constants.RequestCode.STRIPE_PAYMENT -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                Constants.RequestCode.WEB_PAYMENT -> {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        } else {
            if (requestCode == Constants.RequestCode.WEB_PAYMENT || requestCode == Constants.RequestCode.STRIPE_PAYMENT) {
                deleteOrder()
            }
        }
    }

    private fun deleteOrder() {
        if(orderId!=null)
        {
            val or = Order()
            or.id = orderId!!
            getWooApi().deleteOrder(or)

        }
    }


    private fun createOrderRequest(orderRequest: Order) {
        val billing =  getbillingList()
        billing.email = getEmail()
        val shipping = getShippingList()

        orderRequest.billingAddress = billing
        orderRequest.shippingAddress = shipping

        val orderItems: ArrayList<LineItem> = intent.getSerializableExtra(Constants.KeyIntent.PRODUCTDATA) as ArrayList<LineItem>
        orderRequest.line_items = orderItems


        if (shippingItems != null) {
            val itemData = ShippingLine()

            itemData.method_id = shippingItems?.method_id
            itemData.method_title= shippingItems?.method_title

            if (shippingItems!!.method_id == "free_shipping") {
                itemData.total = "0"
            } else {
                if (shippingItems!!.settings.cost.value.isEmpty()) {
                    shippingItems!!.settings.cost.value = "0"
                }
                itemData.total = shippingItems!!.settings.cost.value
            }
            val list = ArrayList<ShippingLine>()
            list.add(itemData)
            orderRequest.shipping_lines = list
        }

        if (mCoupons != null) {
            // TODO: 31/01/2021 copone
         /*   val couponLines: ArrayList<CouponLine> = ArrayList(1)
            val cop = CouponLine()
            cop.code = mCoupons!!.code
            couponLines.add(cop)
            orderRequest.couponLines = couponLines*/
        }

        getWooApi().createOreder(orderRequest).enqueue(object : Callback<Order> {
            override fun onFailure(call: Call<Order>, t: Throwable) {}

            override fun onResponse(call: Call<Order>, response: Response<Order>) {
                val order = response.body()!!
                orderId = order.id
                if (isNativePayment) {
                    startActivityForResult(Intent(this@OrderSummaryActivity,PaymentSuccessfullyActivity::class.java).apply {
                        putExtra(Constants.KeyIntent.ORDER_DATA, order)
                    },Constants.RequestCode.ROZORPAY_PAYMENT)

                    getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_USER_CART)
                    getSharedPrefInstance().removeKey(Constants.SharedPref.KEY_CART_COUNT)
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    getCheckoutUrl(order.id,order.order_key)
                }

                getRoom().Dao().insertOrder(
                        OrderItem(order.id,order.order_key,orderRequest.line_items.first().image)
                )
            }
        })
    }

    private fun getCheckoutUrl(id: Int,order_key: String) {
        val url = Config.siteUrl+"/checkout/order-pay/"+id+"/?pay_for_order=true&key="+order_key
        startActivityForResult(Intent(this@OrderSummaryActivity,WebViewActivity::class.java).apply { putExtra(Constants.KeyIntent.CHECKOUT_URL, url) },Constants.RequestCode.WEB_PAYMENT)
    }

    /* fun onPaymentSuccess(razorpayPaymentId: String?) {
        Log.e(TAG, " onPaymentSuccess")
        try {
            // Create Order request for successfully payment
            val orderRequest = Order()

            orderRequest.payment_method = "razorpay"
            orderRequest.transaction_id = razorpayPaymentId.toString()
            orderRequest.customer_id = getUserId().toInt()
            orderRequest.currency = getDefaultCurrencyFormate()
            orderRequest.status = "pending"
            orderRequest.set_paid = true

            createOrderRequest(orderRequest)
        } catch (e: Exception) {
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
    }*/

    private fun showPaymentFailDialog() {
        val changePasswordDialog = Dialog(this)
        changePasswordDialog.setCancelable(false)
        changePasswordDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        changePasswordDialog.setContentView(R.layout.dialog_failed_transaction)
        changePasswordDialog.window?.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        changePasswordDialog.findViewById<TextView>(R.id.tv_close).setOnClickListener {
            changePasswordDialog.dismiss()
            finish()
        }
        changePasswordDialog.show()
    }

   /* private fun changeColor() {
        tvContinue.backgroundTintList = ColorStateList.valueOf(Color.parseColor(getButtonColor()))
        tvAddressHeading.changeTextPrimaryColor()
        tvUserAddress.changeTextSecondaryColor()
        tvMethod.changeTextSecondaryColor()
        tvMethodName.changeTextSecondaryColor()
        tvChange.changePrimaryColor()
        lblBillingAddress.changeTextPrimaryColor()
        tvBillingAddress.changeTextSecondaryColor()
        lblPaymentMethod.changeTextPrimaryColor()
        rbCard.changeTextSecondaryColor()
        rbRazorpay.changeTextSecondaryColor()
        rbCOD.changeTextSecondaryColor()
        lblSubTotal.changeTextSecondaryColor()
        tvTotal.changeTextSecondaryColor()
        txtDiscountlbl.changeTextSecondaryColor()
        tvDiscount.changeTextSecondaryColor()
        txtShippinglbl.changeTextSecondaryColor()
        tvShipping.changeTextSecondaryColor()
        lblTotalAmount.changeTextPrimaryColor()
        tvTotalCartAmount.changeTextPrimaryColor()
        rlMain.changeBackgroundColor()
    }*/


}