package ma.pam.ajitsowak.woolib.repo.product

import ma.pam.ajitsowak.woolib.data.api.ProductTagAPI
import ma.pam.ajitsowak.woolib.models.Tag
import ma.pam.ajitsowak.woolib.models.filters.ProductTagFilter
import ma.pam.ajitsowak.woolib.repo.WooRepository
import retrofit2.Call

class TagRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: ProductTagAPI

    init {
        apiService = retrofit.create(ProductTagAPI::class.java)
    }

    fun create(tag: Tag): Call<Tag> {
        return apiService.create(tag)
    }


    fun tag(id: Int): Call<Tag> {
        return apiService.view(id)
    }

    fun tags(): Call<List<Tag>> {
        return apiService.list()
    }

    fun tags(productTagFilter: ProductTagFilter): Call<List<Tag>> {
        return apiService.filter(productTagFilter.filters)
    }

    fun update(id: Int, tag: Tag): Call<Tag> {
        return apiService.update(id, tag)
    }

    fun delete(id: Int): Call<Tag> {
        return apiService.delete(id)
    }

    fun delete(id: Int, force: Boolean): Call<Tag> {
        return apiService.delete(id, force)
    }


}
