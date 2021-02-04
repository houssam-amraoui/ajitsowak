package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList


class TaxLine : Serializable {
    var id: Int? = null

    var rate_code: String? = null
    var rate_id: Int = 0

    var label: String? = null

    @SerializedName("compound")
    var isCompound: Boolean = false

    var tax_total: String? = null
    var shipping_tax_total: String? = null

    var meta_data: List<Metum> = ArrayList()
}
