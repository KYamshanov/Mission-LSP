package ru.mission.heart

import android.content.Context
import ru.mission.heart.api.Generator
import ru.mission.heart.storage.Preferences

internal actual fun preferences(): Preferences = PreferencesAndroidImpl(
    checkNotNull(Platform.applicationContext) { "Application context has not initialized yet" }
)

internal actual fun generator(): Generator = GeneratorAndroidImpl()

object Platform {

    internal var applicationContext: Context? = null
        private set


    fun injectApplicationContext(ctx: Context) {
        applicationContext = ctx
    }
}