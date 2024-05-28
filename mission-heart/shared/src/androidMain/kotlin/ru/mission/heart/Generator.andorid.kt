package ru.mission.heart

import android.util.Base64
import io.ktor.utils.io.core.toByteArray
import java.security.MessageDigest
import java.security.SecureRandom
import ru.mission.heart.api.Generator

internal class GeneratorAndroidImpl : Generator {
    override fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.encodeToString(codeVerifier, android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING)

    }

    override fun generateCodeChallenge(codeVerifier: String): String {
        val bytes: ByteArray = codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest = messageDigest.digest()
        return Base64.encodeToString(digest, android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING)
    }

}