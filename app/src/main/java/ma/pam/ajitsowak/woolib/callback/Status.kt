package ma.pam.ajitsowak.woolib.callback

enum class Status {
    EMPTY,
    SUCCESS,
    ERROR,
    LOADING;

    val isLoading: Status
        get() = LOADING
}
