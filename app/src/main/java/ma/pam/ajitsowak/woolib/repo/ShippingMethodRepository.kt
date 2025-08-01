package ma.pam.ajitsowak.woolib.repo

import retrofit2.Call
import ma.pam.ajitsowak.woolib.data.api.ShippingMethodAPI
import ma.pam.ajitsowak.woolib.models.ShippingMethod

class ShippingMethodRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: ShippingMethodAPI

    init {
        apiService = retrofit.create(ShippingMethodAPI::class.java)
    }

    fun shippingMethod(id: String): Call<ShippingMethod> {
        return apiService.view(id)
    }

    fun shippingMethods(): Call<List<ShippingMethod>> {
        return apiService.list()
    }


}
