package ma.pam.ajitsowak.model

import java.io.Serializable

data class Requires(
    var default: String?,
    var description: String?,
    var id: String?,
    var label: String?,
    var options: Options?,
    var placeholder: String?,
    var tip: String?,
    var type: String?,
    var value: String?
): Serializable