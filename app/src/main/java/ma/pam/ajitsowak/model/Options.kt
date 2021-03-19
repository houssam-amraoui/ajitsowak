package ma.pam.ajitsowak.model

import java.io.Serializable

data class Options(
    var both: String?,
    var coupon: String?,
    var either: String?,
    var min_amount: String?
): Serializable