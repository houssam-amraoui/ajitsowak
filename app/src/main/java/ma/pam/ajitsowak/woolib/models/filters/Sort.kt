package ma.pam.ajitsowak.woolib.models.filters

enum class Sort {

    ASCENDING {
        override fun toString(): String {
            return "asc"
        }
    },
    DESCENDING {
        override fun toString(): String {
            return "desc"
        }
    }
}
