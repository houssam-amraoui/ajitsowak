package ma.pam.ajitsowak.woolib.repo

import retrofit2.Call

import ma.pam.ajitsowak.woolib.data.api.PaymentGatewayAPI
import ma.pam.ajitsowak.woolib.models.PaymentGateway

class PaymentGatewayRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: PaymentGatewayAPI

    init {
        apiService = retrofit.create(PaymentGatewayAPI::class.java)
    }

    fun paymentGateway(id: Int): Call<PaymentGateway> {
        return apiService.view(id)
    }

    fun paymentGateways(): Call<List<PaymentGateway>> {
        return apiService.list()
    }

    fun update(id: String, paymentGateway: PaymentGateway): Call<PaymentGateway> {
        return apiService.update(id, paymentGateway)
    }

}
