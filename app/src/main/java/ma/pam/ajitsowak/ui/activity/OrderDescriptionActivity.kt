package ma.pam.ajitsowak.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.woolib.models.LineItem
import ma.pam.ajitsowak.woolib.models.Order
import ma.pam.ajitsowak.woolib.models.OrderNote
import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import ma.pam.ajitsowak.MyApp
import ma.pam.ajitsowak.MyApp.getRoom
import ma.pam.ajitsowak.MyApp.getWooApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OrderDescriptionActivity : AppCompatActivity() {

    private lateinit var orderModel: Order
    private var totalAmt = 0.0
    private var mOrderNoteList = ArrayList<OrderNote>()

    lateinit var cancelOrder: TextView
    lateinit var imgMore: ImageView
    lateinit var tvOrderId: TextView
    lateinit var tvProductName: TextView
    lateinit var tvProductSellerName: TextView
    lateinit var tvPrice: TextView
    lateinit var ivProduct: ImageView
    lateinit var tvUsername: TextView

    lateinit var tvUserAddress: TextView
    lateinit var tvBillingUsername: TextView

    lateinit var tvBillingUserAddress: TextView
    lateinit var tvListPrice: TextView
    lateinit var tvSellingPrice: TextView
    lateinit var tvExtraDiscount: TextView
    lateinit var tvSpecialPrice: TextView
    lateinit var tvShippingFee: TextView
    lateinit var tvTotalAmount: TextView
    lateinit var rlNotes: RelativeLayout

    lateinit var tvTotalPrice:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderdescription)

        cancelOrder = findViewById(R.id.cancelOrder)
        imgMore = findViewById(R.id.imgMore)
        tvOrderId = findViewById(R.id.tvOrderId)
        tvProductName = findViewById(R.id.tvProductName)
        tvProductSellerName = findViewById(R.id.tvProductSellerName)
        tvPrice = findViewById(R.id.tvPrice)
        ivProduct = findViewById(R.id.ivProduct)
        tvUsername = findViewById(R.id.tvUsername)

        tvUserAddress = findViewById(R.id.tvUserAddress)
        tvBillingUsername = findViewById(R.id.tvBillingUsername)

        tvBillingUserAddress = findViewById(R.id.tvBillingUserAddress)
        tvListPrice = findViewById(R.id.tvListPrice)
        tvSellingPrice = findViewById(R.id.tvSellingPrice)
        tvExtraDiscount = findViewById(R.id.tvExtraDiscount)
        tvSpecialPrice = findViewById(R.id.tvSpecialPrice)
        tvShippingFee = findViewById(R.id.tvShippingFee)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        rlNotes= findViewById(R.id.rlNotes)

         tvTotalPrice = findViewById(R.id.tvTotalPrice)

        orderModel = intent.getSerializableExtra(Constants.KeyIntent.DATA) as Order
        title = getString(R.string.lbl_order_title) + orderModel.id.toString() + " " + getString(R.string.lbl_details)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setDetailToolbar(toolbar)


        // mAppBarColor()
        //changeColor()
        val mOrderItemAdapter = BaseAdapter<LineItem>(R.layout.item_order, onBind = { view, model, _ ->
            val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
            val tvProductName: TextView = view.findViewById(R.id.tvProductName)
            val tvPrice: TextView = view.findViewById(R.id.tvPrice)
            val tvOriginalPrice: TextView = view.findViewById(R.id.tvOriginalPrice)


            tvProductName.text = model.name
            //tvProductName.changeTextPrimaryColor()
            tvPrice.text = model.subtotal.currencyFormat(orderModel.currency)
            //tvPrice.changeTextSecondaryColor()
            tvOriginalPrice.text = " Qty: " + model.quantity.toString()
            tvOriginalPrice.visibility = View.VISIBLE
            //tvOriginalPrice.changeTextSecondaryColor()
            totalAmt += model.subtotal.toFloat()

            /* if (model.product_images[0].src.isNotEmpty()) {
                 ivProduct.loadImageFromUrl(model.product_images[0].src)
             }*/

            tvTotalPrice.text = totalAmt.toString().currencyFormat()
            view.setOnClickListener {
                startActivity(Intent(this, ProductDetailActivity1::class.java).apply {
                    // TODO: 02/02/2021 ProductDetailActivity1 add by id
                    putExtra(Constants.KeyIntent.PRODUCT_ID, model.product_id)
                })
            }
        })

        val mOrderNotesAdapter = BaseAdapter<OrderNote>(R.layout.item_track, onBind = { view, model, _ ->
            val tvText1: TextView = view.findViewById(R.id.tvText1)
            val tvText2: TextView = view.findViewById(R.id.tvText2)
            val tvDate: TextView = view.findViewById(R.id.tvDate)

            // val ivLine: ImageView = view.findViewById(R.id.ivLine)
            val ivCircle: ImageView = view.findViewById(R.id.ivCircle)

            try {
                val mValue = model.note.replace("“", "\"").replace("”", "\"")
                if (isJSONValid(mValue)) {
                    tvText1.text = JSONObject(mValue).getString("message")
                    tvText2.text = JSONObject(mValue).getString("status")
                } else {
                    tvText1.text = mValue
                    tvText2.text = "By admin"
                }
            } catch (ex: JSONException) {
                ex.printStackTrace()
            }
            //tvText1.changeTextSecondaryColor()
            // tvText2.changeTextPrimaryColor()
            // tvDate.changeTextSecondaryColor()

            tvDate.text = convertToLocalDate(model.date_created)
            //ivLine.setLineColor(Color.parseColor(getPrimaryColor()))
            // ivCircle.setCircleColor(Color.parseColor(getPrimaryColor()))
        })

        val rvOrderItems: RecyclerView = findViewById(R.id.rvOrderItems)
        val rvOrderNotes: RecyclerView = findViewById(R.id.rvOrderNotes)

        rvOrderItems.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvOrderItems.adapter = mOrderItemAdapter
        mOrderItemAdapter.addItems(orderModel.line_items)
        setDetail()
        rvOrderNotes.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvOrderNotes.adapter = mOrderNotesAdapter

        getWooApi().getOrderNotes(orderModel.id).enqueue(object : Callback<List<OrderNote>>{
            override fun onFailure(call: Call<List<OrderNote>>, t: Throwable) {}
            override fun onResponse(call: Call<List<OrderNote>>, response: Response<List<OrderNote>>) {
                val notes = response.body()
                if (notes.isNullOrEmpty()) {
                    rlNotes.hide()
                } else {
                    notes.forEachIndexed { _, orderNotes ->
                        //if (orderNotes.customer_note) {
                        mOrderNoteList.add(orderNotes)
                        // }
                    }
                    mOrderNotesAdapter.addItems(mOrderNoteList)
                }
            }
        })



        //Check Order date and display Cancel order button

        val calendar: Calendar = Calendar.getInstance()

        calendar.time = orderModel.date_created
        calendar.add(Calendar.HOUR, 1)

        val currentTime = Date(System.currentTimeMillis())

        if (orderModel.status == Constants.OrderStatus.COMPLETED ||
                orderModel.status == Constants.OrderStatus.REFUNDED ||
                orderModel.status == Constants.OrderStatus.CANCELED
        ) {
            cancelOrder.visibility = View.GONE
            imgMore.visibility = View.GONE
        } else {
            if (currentTime.before(calendar.time)) {
                imgMore.visibility = View.VISIBLE
                cancelOrder.visibility = View.VISIBLE
            } else {
                cancelOrder.visibility = View.GONE
                imgMore.visibility = View.GONE
            }
        }
        cancelOrder.setOnClickListener {
            orderCancelPopup()
        }
    }

    // Popup for cancel order

    private fun orderCancelPopup() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.lbl_cancel_order)) // set the custom layout
        val customLayout: View = layoutInflater.inflate(R.layout.dialog_order_cancel, null)
        val status: ArrayList<String> = ArrayList()
        status.add(getString(R.string.order_cancel_1))
        status.add(getString(R.string.order_cancel_2))
        status.add(getString(R.string.order_cancel_3))
        status.add(getString(R.string.order_cancel_4))
        status.add(getString(R.string.order_cancel_5))
        status.add(getString(R.string.order_cancel_6))

        val spinner: Spinner = customLayout.findViewById(R.id.spinner)


        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(applicationContext, R.layout.spinner_items, status)
        spinner.adapter = adapter
        builder.setView(customLayout) // add a button

        builder.setPositiveButton(getString(R.string.lbl_cancel_order)) { dialog, _ ->
            // Call Api for cancel order

            if (isNetworkAvailable()) {

                val orderRequest = Order()
                orderRequest.status = "cancelled"
                orderRequest.customer_note = spinner.selectedItem.toString()

                getWooApi().updateOreder(orderModel.id,orderRequest).enqueue(object: Callback<Order>{
                    override fun onFailure(call: Call<Order>, t: Throwable) { dialog.dismiss()}
                    override fun onResponse(call: Call<Order>, response: Response<Order>) {
                        val notes = OrderNote()
                        notes.isCustomer_note = true
                        notes.note = "{\n" +
                                "\"status\":\"Cancelled\",\n" +
                                "\"message\":\"Order Canceled by you due to " + spinner.selectedItem.toString() + ".\"\n" +
                                "} "
                        getWooApi().addOrderNotes(orderModel.id,notes).enqueue(object: Callback<OrderNote>{
                            override fun onFailure(call: Call<OrderNote>, t: Throwable) {}
                            override fun onResponse(call: Call<OrderNote>, response: Response<OrderNote>) {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        })
                        dialog.dismiss()
                    }
                })



            } else {
                // TODO: 02/02/2021 no internet
                dialog.dismiss()
            }

        }

        builder.setNegativeButton(getString(R.string.lbl_close)) { _, _ ->
        }
        builder.show()
    }

    private fun isJSONValid(test: String): Boolean {
        try {
            JSONObject(test)
        } catch (ex: JSONException) {
            return false
        }
        return true
    }

    private fun setDetail() {
        try {
            if (orderModel.date_paid != null) {
                if (orderModel.transaction_id.isEmpty()) {
                    tvOrderId.text = getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method + " (" + orderModel.transaction_id + "). " + getString(R.string.lbl_paid_on) + " " + convertOrderDataToLocalDate(orderModel.date_paid)
                } else {
                    tvOrderId.text = getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method + ". " + getString(R.string.lbl_paid_on) + " " + convertOrderDataToLocalDate(orderModel.date_paid)
                }

            } else {
                tvOrderId.text = getString(R.string.lbl_transaction_via) + " " + orderModel.payment_method
            }

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        tvProductName.text = orderModel.line_items[0].name
        tvProductSellerName.text = orderModel.shippingAddress.firstName

        tvPrice.text = orderModel.line_items[0].total.currencyFormat()

        // TODO: 02/02/2021 add
        //ivProduct.loadImageFromUrl(orderModel.line_items[0].product_images[0].src)

        // Shipping information

        tvUsername.text = orderModel.shippingAddress.firstName + " " + orderModel.shippingAddress.lastName
        var shippingAddress = ""
        if (!orderModel.shippingAddress.address1.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shippingAddress.address1
            } else {
                orderModel.shippingAddress.address1!!
            }
        }

        if (!orderModel.shippingAddress.address2.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shippingAddress.address2
            } else {
                orderModel.shippingAddress.address2!!
            }
        }

        if (!orderModel.shippingAddress.city.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shippingAddress.city
            } else {
                orderModel.shippingAddress.city!!
            }
        }

        if (!orderModel.shippingAddress.postcode.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + " - " + orderModel.shippingAddress.postcode
            } else {
                orderModel.shippingAddress.postcode!!
            }
        }

        if (!orderModel.shippingAddress.state.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shippingAddress.state
            } else {
                orderModel.shippingAddress.state!!
            }
        }


        if (!orderModel.shippingAddress.country.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + orderModel.shippingAddress.country
            } else {
                orderModel.shippingAddress.country!!
            }
        }



        if (!orderModel.billingAddress.phone.isNullOrEmpty()) {
            shippingAddress = if (shippingAddress.isNotBlank()) {
                shippingAddress + "\n" + getString(R.string.lbl_phone_number) + orderModel.billingAddress.phone
            } else {
                orderModel.billingAddress.phone!!
            }
        }
        tvUserAddress.text = shippingAddress

        //Billing information

        var billingAddress = ""
        tvBillingUsername.text = orderModel.billingAddress.firstName + " " + orderModel.billingAddress.lastName
        if (!orderModel.billingAddress.address1.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + "\n" + orderModel.billingAddress.address1
            } else {
                orderModel.billingAddress.address1!!
            }
        }

        if (!orderModel.billingAddress.address2.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + "\n" + orderModel.billingAddress.address2
            } else {
                orderModel.billingAddress.address2!!
            }
        }

        if (!orderModel.billingAddress.city.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + "\n" + orderModel.billingAddress.city
            } else {
                orderModel.billingAddress.city!!
            }
        }

        if (!orderModel.billingAddress.postcode.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + " - " + orderModel.billingAddress.postcode
            } else {
                orderModel.billingAddress.postcode!!
            }
        }

        if (!orderModel.billingAddress.state.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + "\n" + orderModel.billingAddress.state
            } else {
                orderModel.billingAddress.state!!
            }
        }

        if (!orderModel.billingAddress.country.isNullOrEmpty()) {
            billingAddress = if (billingAddress.isNotBlank()) {
                billingAddress + "\n" + orderModel.billingAddress.country
            } else {
                orderModel.billingAddress.country!!
            }
        }

        tvBillingUserAddress.text = billingAddress
        tvListPrice.text = "0".currencyFormat()
        tvSellingPrice.text = "0".currencyFormat()
        tvExtraDiscount.text = orderModel.discount_total.currencyFormat()
        tvSpecialPrice.text = "0".currencyFormat()
        tvShippingFee.text = orderModel.shipping_total.currencyFormat()
        tvTotalAmount.text = orderModel.total.currencyFormat()

    }

    /* private fun changeColor() {
         cancelOrder.changePrimaryColor()
         lblShippingDetail.changeTextPrimaryColor()
         lblItemOrder.changeTextPrimaryColor()
         lblPriceDetail.changeTextPrimaryColor()
         lblExtraDiscount.changeTextSecondaryColor()
         lblSpecialPrice.changeTextSecondaryColor()
         lblShippingFee.changeTextSecondaryColor()
         lblTotalAmount.changeTextPrimaryColor()
         tvOrderId.changeTextSecondaryColor()
         tvProductName.changeTextPrimaryColor()
         tvProductSellerName.changeTextPrimaryColor()
         tvPrice.changeTextPrimaryColor()
         tvUsername.changeTextPrimaryColor()
         tvUserAddress.changeTextSecondaryColor()
         lblAmount.changeTextSecondaryColor()
         tvTotalPrice.changeTextPrimaryColor()
         tvListPrice.changeTextPrimaryColor()
         tvSellingPrice.changeTextPrimaryColor()
         tvExtraDiscount.changeTextPrimaryColor()
         tvSpecialPrice.changeTextPrimaryColor()
         tvShippingFee.changeTextPrimaryColor()
         tvTotalAmount.changeTextPrimaryColor()
         rlMain.changeBackgroundColor()
     }*/


}
