package ma.pam.ajitsowak.woolib.data.auth

class OAuthSignatureException
    (stringToSign: String, e: Exception) : OAuthException(String.format(MSG, stringToSign), e) {
    companion object {
        private val serialVersionUID = 1L
        private val MSG = "Error while signing string: %s"
    }

}