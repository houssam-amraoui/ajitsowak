package ma.pam.ajitsowak.woolib.data.auth

interface TimestampService {
    val timestampInSeconds: String
    val nonce: String
}