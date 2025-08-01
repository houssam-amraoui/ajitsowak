package ma.pam.ajitsowak.model

import java.io.Serializable

data class Settings(
        var cost: Cost = Cost(),
        var ignore_discounts: IgnoreDiscounts? = null,
        var min_amount: MinAmount? = null,
        var requires: Requires? = null,
        var tax_status: TaxStatus? = null,
        var title: Title? = null
): Serializable