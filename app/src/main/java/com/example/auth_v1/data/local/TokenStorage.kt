package com.example.auth_v1.data.local

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenStorage @Inject constructor(
    private val encryptedSharedPreferences: EncryptedSharedPreferences
) {

    fun saveAccessToken(token: String) {
        encryptedSharedPreferences.edit {
            putString(ACCESS_TOKEN_KEY, token)
        }
    }

    fun getAccessToken(): String? {
        return encryptedSharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }
    
    fun saveRefreshToken(token: String) {
        encryptedSharedPreferences.edit {
            putString(REFRESH_TOKEN_KEY, token)
        }
    }
    
    fun getRefreshToken(): String? {
        return encryptedSharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    fun saveTokenExpiration(expirationTime: Long) {
        encryptedSharedPreferences.edit {
            putLong(TOKEN_EXPIRATION_KEY, expirationTime)
        }
    }

    fun getTokenExpiration(): Long {
        return encryptedSharedPreferences.getLong(TOKEN_EXPIRATION_KEY, 0)
    }

    fun clearTokens() {
        encryptedSharedPreferences.edit {
            remove(ACCESS_TOKEN_KEY)
                .remove(REFRESH_TOKEN_KEY)
                .remove(TOKEN_EXPIRATION_KEY)
        }
    }

    fun hasValidToken(): Boolean {
        val token = getAccessToken()
        val expiration = getTokenExpiration()
        return token != null && System.currentTimeMillis() < expiration
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val TOKEN_EXPIRATION_KEY = "token_expiration"
    }
}