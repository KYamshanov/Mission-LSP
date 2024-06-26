package ru.mission.heart

import android.util.Base64
import io.ktor.utils.io.core.toByteArray
import java.security.MessageDigest
import java.security.SecureRandom

internal class AndroidGeneratorImpl : Generator {
    override fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.encodeToString(
            /* input = */ codeVerifier,
            /* flags = */ android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

    override fun generateCodeChallenge(codeVerifier: String): String {
        val bytes: ByteArray = codeVerifier.toByteArray(Charsets.US_ASCII)
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes, 0, bytes.size)
        val digest = messageDigest.digest()
        return Base64.encodeToString(
            /* input = */ digest,
            /* flags = */ android.util.Base64.URL_SAFE or android.util.Base64.NO_PADDING or Base64.NO_WRAP
        )
    }

}