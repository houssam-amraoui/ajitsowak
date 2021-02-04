package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList


class ShippingLine : Serializable {

    var id: Int? = null
    var method_title: String? = null
    var method_id: String? = null
    var total: String? = null
    var total_tax: String? = null
    var taxes: List<TaxLine> = ArrayList()

    var meta_data: List<Metum> = ArrayList()
}
