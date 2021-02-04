package ma.pam.ajitsowak.woolib.models.filters

import ma.pam.ajitsowak.woolib.models.filters.ListFilter

class WebhookFilter : ListFilter() {

    internal lateinit var status: String

    fun getStatus(): String {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
        addFilter("status", status)
    }
}
