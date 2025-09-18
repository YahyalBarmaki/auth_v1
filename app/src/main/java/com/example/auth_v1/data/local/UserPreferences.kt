package com.example.auth_v1.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.example.auth_v1.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    // User data keys
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_EMAIL = stringPreferencesKey("user_email")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_PROFILE_PICTURE = stringPreferencesKey("user_profile_picture")
    private val USER_EMAIL_VERIFIED = booleanPreferencesKey("user_email_verified")
    private val USER_CREATED_AT = stringPreferencesKey("user_created_at")

    // App preferences keys
    private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
    private val REMEMBER_ME = booleanPreferencesKey("remember_me")
    private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    private val THEME_MODE = stringPreferencesKey("theme_mode")

    // User data flows
    val userFlow: Flow<User?> = dataStore.data.map { preferences ->
        val id = preferences[USER_ID]
        val email = preferences[USER_EMAIL]
        val name = preferences[USER_NAME]

        if (id != null && email != null && name != null) {
            User(
                id = id,
                email = email,
                name = name,
                profilePictureUrl = preferences[USER_PROFILE_PICTURE],
                isEmailVerified = preferences[USER_EMAIL_VERIFIED] ?: false,
                createdAt = preferences[USER_CREATED_AT]
            )
        } else null
    }
    val isFirstLaunchFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[IS_FIRST_LAUNCH] ?: true
    }

    val rememberMeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[REMEMBER_ME] ?: false
    }

    val biometricEnabledFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[BIOMETRIC_ENABLED] ?: false
    }

    val themeModeFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[THEME_MODE] ?: "system"
    }

    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_EMAIL] = user.email
            preferences[USER_NAME] = user.name
            user.profilePictureUrl?.let { preferences[USER_PROFILE_PICTURE] = it }
            preferences[USER_EMAIL_VERIFIED] = user.isEmailVerified
            user.createdAt?.let { preferences[USER_CREATED_AT] = it }
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_NAME)
            preferences.remove(USER_PROFILE_PICTURE)
            preferences.remove(USER_EMAIL_VERIFIED)
            preferences.remove(USER_CREATED_AT)
        }
    }

    suspend fun setFirstLaunchCompleted() {
        dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    suspend fun setRememberMe(remember: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMEMBER_ME] = remember
        }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[BIOMETRIC_ENABLED] = enabled
        }
    }

    suspend fun setThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode
        }
    }
}