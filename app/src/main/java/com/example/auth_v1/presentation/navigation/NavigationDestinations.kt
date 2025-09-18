package com.example.auth_v1.presentation.navigation

object NavigationDestinations {

    // Auth Routes
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT_PASSWORD = "forgot_password"
    const val EMAIL_VERIFICATION = "email_verification"

    // Main App Routes
    const val MAIN_GRAPH = "main_graph"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val SETTINGS = "settings"

    // Route with arguments
    const val PROFILE_WITH_ID = "profile/{userId}"
    const val EMAIL_VERIFICATION_WITH_EMAIL = "email_verification/{email}"

    // Deep Link Routes
    const val RESET_PASSWORD_DEEP_LINK = "letsconnect://reset-password/{token}"
    const val EMAIL_VERIFY_DEEP_LINK = "letsconnect://verify-email/{token}"
}

// Navigation Arguments
object NavigationArgs {
    const val USER_ID = "userId"
    const val EMAIL = "email"
    const val TOKEN = "token"
}

// Route Builder Functions
object RouteBuilder {
    fun profileWithId(userId: String): String {
        return "profile/$userId"
    }

    fun emailVerificationWithEmail(email: String): String {
        return "email_verification/$email"
    }

    fun resetPasswordWithToken(token: String): String {
        return "letsconnect://reset-password/$token"
    }

    fun emailVerifyWithToken(token: String): String {
        return "letsconnect://verify-email/$token"
    }
}