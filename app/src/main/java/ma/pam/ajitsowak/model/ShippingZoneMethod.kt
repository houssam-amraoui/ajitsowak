package ma.pam.ajitsowak.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ShippingZoneMethod(
        @SerializedName("enabled")
    var isEnabled: Boolean = false,
        var id: Int? = null,
        var instance_id: Int? = null,
        var method_description: String? = null,
        var method_id: String? = null,
        var method_title: String? = null,
        var order: Int? = null,
        var settings: Settings = Settings(),
        var title: String? = null
) : Serializable