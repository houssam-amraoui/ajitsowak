package ma.pam.ajitsowak.woolib.repo.product

import ma.pam.ajitsowak.woolib.data.api.ProductReviewAPI
import ma.pam.ajitsowak.woolib.models.ProductReview
import ma.pam.ajitsowak.woolib.models.filters.ProductReviewFilter
import ma.pam.ajitsowak.woolib.repo.WooRepository
import retrofit2.Call

class ReviewRepository(baseUrl: String, consumerKey: String, consumerSecret: String) :
    WooRepository(baseUrl, consumerKey, consumerSecret) {

    private val apiService: ProductReviewAPI

    init {
        apiService = retrofit.create(ProductReviewAPI::class.java)
    }

    fun create(review: ProductReview): Call<ProductReview> {
        return apiService.create(review)
    }


    fun review(id: Int): Call<ProductReview> {
        return apiService.view(id)
    }

    fun reviews(): Call<List<ProductReview>> {
        return apiService.list()
    }

    fun reviews(productReviewFilter: ProductReviewFilter): Call<List<ProductReview>> {
        return apiService.filter(productReviewFilter.filters)
    }

    fun update(id: Int, review: ProductReview): Call<ProductReview> {
        return apiService.update(id, review)
    }

    fun delete(id: Int): Call<ProductReview> {
        return apiService.delete(id)
    }

    fun delete(id: Int, force: Boolean): Call<ProductReview> {
        return apiService.delete(id, force)
    }


}
