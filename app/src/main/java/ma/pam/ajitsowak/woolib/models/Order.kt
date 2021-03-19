package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

import java.util.ArrayList
import java.util.Date


class Order : Serializable {
    var id: Int = 0
    var parent_id:Int = 0
    @SerializedName("number")
    lateinit var orderNumber: String
    var order_key:String = ""
    var created_via:String = ""
    lateinit var status: String
    lateinit var currency: String

    lateinit var date_created: Date

    var date_modified: Date?=null

    var discount_total:String = ""

    var discount_tax:String = ""
    var shipping_total:String = ""

    lateinit var shipping_tax: String

    lateinit var cart_tax: String

    lateinit var total: String

    lateinit var total_tax: String

    lateinit var customer_user_agent: String
    var customer_id: Int? = null
    lateinit var customer_ip_address: String

    lateinit var customer_note: String //Note left by customer during checkout.

    @SerializedName("billing")
    lateinit var billingAddress: BillingAddress
    @SerializedName("shipping")
    lateinit var shippingAddress: ShippingAddress


    lateinit var payment_method: String
    lateinit var payment_method_title: String
    var transaction_id: String = ""
    var date_paid: Date? =null
    var set_paid: Boolean = false

    var meta_data: List<Metum> = ArrayList()

    var line_items: MutableList<LineItem> = ArrayList()

    var tax_lines: List<TaxLine> = ArrayList()

    var shipping_lines: List<ShippingLine> = ArrayList()

   // var fee_lines: List<FeeLine> = ArrayList() // TODO: 01/02/2021 reper
    var coupon_lines: List<Any> = ArrayList() // TODO: 01/02/2021 reper


    fun addLineItem(lineItem: LineItem) {
        line_items.add(lineItem)
    }
}
