package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

import java.util.ArrayList


class LineItem :Serializable {
    var id:Int? = null
    lateinit var name: String
    var product_id: Int = 0
    lateinit var variation_id: String
    var quantity: Int = 0
    lateinit var subtotal: String //before discounts

    lateinit var subtotal_tax: String
    lateinit var total: String
    lateinit var total_tax: String
    lateinit var price: String

    lateinit var taxes: Any
    lateinit var sku: String

    var meta_data: List<Metum> = ArrayList()
    @Expose(serialize = false,deserialize = false)
    var image:String = ""
}
