package ma.pam.ajitsowak.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItem (
        @PrimaryKey
        var productId: Int = 0,
        var variationId: Int = 0,
        var quantity: Int = 0,
        var productName: String = "",
        var price: String = "",
        var sale_price: String = "",
        var regular_price: String = "",
        var productImage: String = ""

)


