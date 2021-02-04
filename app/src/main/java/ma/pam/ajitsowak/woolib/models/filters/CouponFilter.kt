package ma.pam.ajitsowak.woolib.models.filters

class CouponFilter : ListFilter() {

    internal lateinit var code: String

    fun getCode(): String {
        return code
    }

    fun setCode(code: String) {
        this.code = code
        addFilter("code", code)
    }
}
