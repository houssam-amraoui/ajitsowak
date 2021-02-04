package ma.pam.ajitsowak.woolib.repo.product

import ma.pam.ajitsowak.woolib.data.api.ProductAttributeAPI
import ma.pam.ajitsowak.woolib.models.ProductAttribute
import ma.pam.ajitsowak.woolib.models.filters.ProductAttributeFilter
import ma.pam.ajitsowak.woolib.repo.WooRepository
import retrofit2.Call

class AttributeRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: ProductAttributeAPI = retrofit.create(ProductAttributeAPI::class.java)

    fun create(productAttribute: ProductAttribute): Call<ProductAttribute> {
        return apiService.create(productAttribute)
    }


    fun attribute(id: Int): Call<ProductAttribute> {
        return apiService.view(id)
    }

    fun attributes(): Call<List<ProductAttribute>> {
        return apiService.list()
    }

    fun attributes(productAttributeFilter: ProductAttributeFilter): Call<List<ProductAttribute>> {
        return apiService.filter(productAttributeFilter.filters)
    }

    fun update(id: Int, productAttribute: ProductAttribute): Call<ProductAttribute> {
        return apiService.update(id, productAttribute)
    }

    fun delete(id: Int): Call<ProductAttribute> {
        return apiService.delete(id)
    }

    fun delete(id: Int, force: Boolean): Call<ProductAttribute> {
        return apiService.delete(id, force)
    }


}
