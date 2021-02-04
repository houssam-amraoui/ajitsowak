package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName

import java.io.Serializable
import java.util.*


class Customer : Serializable {
    var id: Int = 0

    @SerializedName("date_created")
    var date_created: Date?= null

    var email: String?= null

    @SerializedName("first_name")
    var firstName: String?= null

    @SerializedName("last_name")
    var lastName: String?= null

    var username: String?= null
    var password: String?= null
    var role: String?= null

    @SerializedName("last_order_id")
    var lastOrderId: String?= null

    @SerializedName("last_order_date")
    var lastOrderDate: String?= null

    @SerializedName("orders_count")
    var ordersCount: Int = 0

    @SerializedName("total_spent")
    var totalSpent: String?= null

    @SerializedName("avatar_url")
    var avatarUrl: String?= null

    @SerializedName("billing")
    var billingAddress: BillingAddress?= null

    @SerializedName("shipping")
    var shippingAddress: ShippingAddress?= null


}
