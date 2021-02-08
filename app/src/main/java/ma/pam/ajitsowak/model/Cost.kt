package ma.pam.ajitsowak.model

data class Cost(
    var default: String? = null,
    var description: String? = null,
    var id: String? = null,
    var label: String? = null,
    var placeholder: String? = null,
    var tip: String? = null,
    var type: String? = null,
    var value: String = "0"
)