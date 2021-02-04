package ma.pam.ajitsowak.woolib.data.callbacks

import com.google.gson.annotations.SerializedName
import ma.pam.ajitsowak.woolib.models.Store

class StoreCallback {

    @SerializedName("store")
    lateinit var store: Store
}
