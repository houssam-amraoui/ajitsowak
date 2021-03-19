package ma.pam.ajitsowak.model

import java.io.Serializable

data class IgnoreDiscounts(
    var default: String?,
    var description: String?,
    var id: String?,
    var label: String?,
    var placeholder: String?,
    var tip: String?,
    var type: String?,
    var value: String?
): Serializable