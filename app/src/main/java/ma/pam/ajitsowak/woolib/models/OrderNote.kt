package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName
import java.util.*


class OrderNote {
    var id: Int = 0
    lateinit var author: String
    lateinit var date_created: Date
    lateinit var date_created_gmt: String
    lateinit var note: String
    @SerializedName("customer_note")
    var isCustomer_note: Boolean = false
}
