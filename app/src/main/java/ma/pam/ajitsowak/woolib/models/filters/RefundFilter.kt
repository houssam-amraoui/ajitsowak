package ma.pam.ajitsowak.woolib.models.filters

import ma.pam.ajitsowak.woolib.models.filters.ListFilter

class RefundFilter : ListFilter() {

    var parent: IntArray? = null
        set(parent) {
            field = parent
            if (parent != null) {
                addFilter("parent", parent)
            }
        }
    var parent_exclude: IntArray? = null
        set(parent_exclude) {
            field = parent_exclude

            if (parent_exclude != null) {
                addFilter("parent_exclude", parent_exclude)
            }
        }

    internal var dp: Int = 0

    fun getDp(): Int {
        return dp
    }

    fun setDp(dp: Int) {
        this.dp = dp
        addFilter("dp", dp)

    }
}
