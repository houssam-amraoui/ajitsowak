package ma.pam.ajitsowak.woolib.repo.product

import ma.pam.ajitsowak.woolib.data.api.ProductVariationAPI
import ma.pam.ajitsowak.woolib.models.Variation
import ma.pam.ajitsowak.woolib.models.filters.ProductVariationFilter
import ma.pam.ajitsowak.woolib.repo.WooRepository
import retrofit2.Call

class VariationRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: ProductVariationAPI

    init {
        apiService = retrofit.create(ProductVariationAPI::class.java)
    }

    fun create(product_id: Int, variation: Variation): Call<Variation> {
        return apiService.create(product_id, variation)
    }


    fun variation(product_id: Int, id: Int): Call<Variation> {
        return apiService.view(product_id, id)
    }

    fun variations(product_id: Int): Call<List<Variation>> {
        return apiService.list(product_id)
    }

    fun variations(product_id: Int, productVariationFilter: ProductVariationFilter): Call<List<Variation>> {
        return apiService.filter(product_id, productVariationFilter.filters)
    }

    fun update(product_id: Int, id: Int, variation: Variation): Call<Variation> {
        return apiService.update(product_id, id, variation)
    }

    fun delete(product_id: Int, id: Int): Call<Variation> {
        return apiService.delete(product_id, id)
    }

    fun delete(product_id: Int, id: Int, force: Boolean): Call<Variation> {
        return apiService.delete(product_id, id, force)
    }

}
