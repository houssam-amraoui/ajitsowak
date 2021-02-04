package ma.pam.ajitsowak.woolib.models.filters

import ma.pam.ajitsowak.woolib.models.filters.Filter

class OrderNoteFilter : Filter() {

    internal lateinit var type: String

    fun getType(): String {
        return type
    }

    fun setType(type: String) {
        this.type = type

        addFilter("type", type)
    }
}
