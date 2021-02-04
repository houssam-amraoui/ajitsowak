package ma.pam.ajitsowak.woolib.models

import java.io.Serializable

class Category : Serializable {
    var id: Int = 0
    var name: String? = null
    var slug: String? = null
    var parent: Int = 0
    var description: String? = null
    var display: String? = null
    var image: Image? = null
    var menu_order: Int = 0
    var count: Int = 0
}
