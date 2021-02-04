package ma.pam.ajitsowak.woolib.data.callbacks


import com.google.gson.annotations.SerializedName
import ma.pam.ajitsowak.woolib.models.ProductReview

import java.util.ArrayList


class ReviewsCallback {
    @SerializedName("product_reviews")
    lateinit var productReviews: ArrayList<ProductReview>
}
