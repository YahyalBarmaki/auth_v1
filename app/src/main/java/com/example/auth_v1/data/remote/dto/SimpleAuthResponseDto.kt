package com.example.auth_v1.data.remote.dto

import android.util.Log
import com.example.auth_v1.domain.model.AuthResponse
import com.example.auth_v1.domain.model.User
import kotlinx.serialization.Serializable

@Serializable
data class SimpleAuthResponseDto(
    val token: String
)

fun SimpleAuthResponseDto.toDomain(): AuthResponse {
    Log.d("JWT", "Token à décoder: $token")
    // Décodez le JWT pour extraire les infos utilisateur
    val payload = decodeJwtPayload(token)
    Log.d("JWT", "Payload décodé: $payload")
    
    // Extraire l'expiration du JWT
    val exp = payload["exp"]?.toLongOrNull() ?: 0
    val currentTime = System.currentTimeMillis() / 1000 // Convertir en secondes
    val expiresIn = if (exp > currentTime) exp - currentTime else 30 * 60 // 30 minutes par défaut
    
    Log.d("JWT", "Token expire dans: $expiresIn secondes (exp: $exp, now: $currentTime)")
    
    return AuthResponse(
        user = User(
            id = payload["sub"] ?: "",
            email = payload["email"] ?: "",
            name = payload["name"] ?: "",
            profilePictureUrl = null,
            isEmailVerified = true,
            createdAt = null
        ),
        accessToken = token,
        refreshToken = "", // Si pas de refresh token
        expiresIn = expiresIn
    )
}

// Fonction simple pour décoder le payload JWT (sans vérification)
private fun decodeJwtPayload(token: String): Map<String, String> {
    return try {
        val parts = token.split(".")
        Log.d("JWT", "Parties du token: ${parts.size}")
        if (parts.size >= 2) {
            val payload = String(android.util.Base64.decode(parts[1], android.util.Base64.URL_SAFE))
            Log.d("JWT", "Payload JSON: $payload")
            // Parse JSON basique - vous pouvez utiliser kotlinx.serialization
            mapOf(
                "sub" to extractJsonValue(payload, "sub"),
                "email" to extractJsonValue(payload, "email"),
                "name" to extractJsonValue(payload, "name"),
                "exp" to extractJsonNumber(payload, "exp")
            )
        } else {
            Log.e("JWT", "Token invalide - pas assez de parties")
            emptyMap()
        }
    } catch (e: Exception) {
        Log.e("JWT", "Erreur lors du décodage: ${e.message}")
        emptyMap()
    }
}

private fun extractJsonValue(json: String, key: String): String {
    val regex = "\"$key\"\\s*:\\s*\"([^\"]+)\"".toRegex()
    return regex.find(json)?.groupValues?.get(1) ?: ""
}

private fun extractJsonNumber(json: String, key: String): String {
    val regex = "\"$key\"\\s*:\\s*(\\d+)".toRegex()
    return regex.find(json)?.groupValues?.get(1) ?: ""
}