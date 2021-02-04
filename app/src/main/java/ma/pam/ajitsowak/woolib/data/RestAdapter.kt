package ma.pam.ajitsowak.woolib.data


import java.util.ArrayList

class RestAdapter(private val baseUrl: String, private val consumerKey: String, private val consumerSecret: String) {
    companion object {

        internal var oauth_nonce = ""
        internal var oauth_timestamp = ""
        internal var oauth_signature_method = "HMAC-SHA1"

    }


}
