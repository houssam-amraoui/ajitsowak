package ma.pam.ajitsowak.woolib.data.api


import retrofit2.Call
import ma.pam.ajitsowak.woolib.models.PaymentGateway
import retrofit2.http.*

interface PaymentGatewayAPI {


    @GET("payment_gateways/{id}")
    fun view(@Path("id") id: Int): Call<PaymentGateway>

    @GET("payment_gateways")
    fun list(): Call<List<PaymentGateway>>

    @Headers("Content-Type: application/json")
    @PUT("payment_gateways")
    fun update(@Path("id") id: String, @Body body: PaymentGateway): Call<PaymentGateway>

}