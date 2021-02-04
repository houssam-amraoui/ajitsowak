package ma.pam.ajitsowak.room

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class OrderItem (
    @PrimaryKey
    var orderId: Int = 0,
    var orderKey: String = "",
    var orderImage: String = ""
)