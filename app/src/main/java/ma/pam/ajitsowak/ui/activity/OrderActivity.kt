package ma.pam.ajitsowak.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import ma.pam.ajitsowak.MyApp
import ma.pam.ajitsowak.MyApp.getWooApi
import ma.pam.ajitsowak.R
import ma.pam.ajitsowak.adapter.BaseAdapter
import ma.pam.ajitsowak.room.FavModel
import ma.pam.ajitsowak.room.OrderItem
import ma.pam.ajitsowak.utils.*
import ma.pam.ajitsowak.utils.Constants.KeyIntent.DATA
import ma.pam.ajitsowak.utils.Constants.SharedPref.KEY_ORDER_COUNT
import ma.pam.ajitsowak.woolib.models.Order
import ma.pam.ajitsowak.woolib.models.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import kotlin.math.roundToInt

@SuppressLint("SetTextI18n")
class OrderActivity : AppCompatActivity() {

    private val mOrderAdapter = BaseAdapter<Order>(R.layout.item_orderlist, onBind = { view, model, position ->
        val ivProduct: ImageView = view.findViewById(R.id.ivProduct)
        val tvProductName: TextView = view.findViewById(R.id.tvProductName)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvInfo: TextView = view.findViewById(R.id.tvInfo)
        val tvProductDeliveryDate: TextView = view.findViewById(R.id.tvProductDeliveryDate)
        val llDeliveryDate: LinearLayout = view.findViewById(R.id.llDeliveryDate)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val rlMainOrder: RelativeLayout = view.findViewById(R.id.rlMainOrder)

        if (model.line_items.isNotEmpty()) {
             if (orders[position].orderImage.isNotEmpty()) {
                 ivProduct.loadImageFromUrl(orders[position].orderImage)
             }
            if (model.line_items.size > 1) {
                tvProductName.text = model.line_items[0].name + " + " + (model.line_items.size - 1) + " " + getString(R.string.lbl_more_item)
            } else {
                tvProductName.text = model.line_items[0].name
            }
        } else {
            tvProductName.text = getString(R.string.lbl_order_title) + model.id
        }

        tvPrice.text = (model.total.toFloat() - model.discount_total.toFloat()).roundToInt().toString().currencyFormat(model.currency)

        try {
            if (model.date_paid != null) {
                if (model.transaction_id.isEmpty()) {

                    tvInfo.text = getString(R.string.lbl_transaction_via) + " " + model.payment_method + " (" + model.transaction_id + "). " + getString(R.string.lbl_paid_on) + " " + convertOrderDataToLocalDate(model.date_paid)
                } else {
                    tvInfo.text = getString(R.string.lbl_transaction_via) + " " + model.payment_method + ". " + getString(R.string.lbl_paid_on) + " " + convertOrderDataToLocalDate(model.date_paid)
                }
            } else {
                tvInfo.text = getString(R.string.lbl_transaction_via) + " " + model.payment_method
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        if (model.status == Constants.OrderStatus.COMPLETED) {
            tvProductDeliveryDate.text = convertOrderDataToLocalDate(model.date_modified)
        } else {
            llDeliveryDate.hide()
        }

        tvStatus.text = model.status
        rlMainOrder.setOnClickListener {
            startActivityForResult(Intent(this, OrderDescriptionActivity::class.java).apply { putExtra(DATA, model) }, Constants.RequestCode.ORDER_CANCEL)
        }

        /* view.tvProductName.changeTextPrimaryColor()
         view.tvInfo.changeTextSecondaryColor()
         view.lblDelivery.changeTextSecondaryColor()
         view.tvProductDeliveryDate.changeTextPrimaryColor()
         view.tvPrice.changePrimaryColor()
         view.tvStatus.changeTextPrimaryColor()
         view.tvStatus.changeTextPrimaryColor()*/
    })

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.RequestCode.ORDER_CANCEL) {
                getOrderList()
            }
        }
    }



    var orders:List<OrderItem> = ArrayList()

    private lateinit var rvOrder: RecyclerView
    lateinit var rlNoData: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        title = getString(R.string.lbl_my_orders)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setDetailToolbar(toolbar)

        rvOrder = findViewById(R.id.rvOrder)
        rlNoData = findViewById(R.id.rlNoData)

        // mAppBarColor()
        // rlMain.changeBackgroundColor()
        //disableHardwareRendering(rvOrder)
        rvOrder.adapter = mOrderAdapter

        getOrderList()
    }

    private fun getOrderList() {

        orders = MyApp.getRoom().Dao().orderList

        if (orders.isEmpty()) {
            rlNoData.show()
        } else {
            val temp = IntArray(orders.size)
            orders.forEachIndexed { index, order ->
                temp[index] = order.orderId
            }
            getWooApi().getOrderIncludeId(temp).enqueue(object : Callback<List<Order>> {
                override fun onFailure(call: Call<List<Order>>, t: Throwable) {}
                override fun onResponse(call: Call<List<Order>>, response: Response<List<Order>>) {
                    if (response.body() != null) {
                        rlNoData.hide()
                        mOrderAdapter.clearItems()
                        mOrderAdapter.addItems(response.body()!!)
                        getSharedPrefInstance().setValue(KEY_ORDER_COUNT, orders.size)
                    }
                }
            })
        }
    }
}
