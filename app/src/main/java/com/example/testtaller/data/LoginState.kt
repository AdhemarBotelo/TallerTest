data class LoginState(
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: Boolean = false,
    val isLogued: Boolean = false
)