package ma.pam.ajitsowak.woolib.data.callbacks


import com.google.gson.annotations.SerializedName
import ma.pam.ajitsowak.woolib.models.Category


import java.util.ArrayList


class CategoriesCallback {
    @SerializedName("product_categories")
    lateinit var categories: ArrayList<Category>
}
