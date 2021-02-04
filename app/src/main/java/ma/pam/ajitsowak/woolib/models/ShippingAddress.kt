package ma.pam.ajitsowak.woolib.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ShippingAddress :Serializable {
    var id: Int = 0
    @SerializedName("first_name")
    var firstName: String? = null
    @SerializedName("last_name")
    var lastName: String? = null
    var company: String? = null
    @SerializedName("address_1")
    var address1: String? = null
    @SerializedName("address_2")
    var address2: String? = null
    var city: String? = null
    var state: String? = null
    var postcode: String? = null
    var country: String? = null

    override fun toString(): String {
        return (firstName + " " + lastName + "\n" +
                address1 + " " + address2 + "\n"
                + city + ", " + state + " " + postcode + "\n"
                + country)
    }

   fun getFullAddress(sap:String =","):String {
       return "$address1$sap$address2$sap$city $postcode$sap$country"
   }




}
