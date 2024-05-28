package ru.mission.heart

import android.content.Context
import android.content.Context.MODE_PRIVATE
import ru.mission.heart.storage.Preferences


internal class PreferencesAndroidImpl constructor(
    applicationContext: Context,
) : Preferences {

    private val preferences by lazy {
        applicationContext.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            MODE_PRIVATE
        )
    }

    override fun saveValue(key: String, value: String) {
        preferences.edit()
            .apply {
                putString(key, value)
            }.apply()
    }

    override fun getValue(key: String): String? {
        return preferences.getString(key, null)
    }

    override fun remove(key: String) {
        preferences.edit()
            .apply {
                remove(key)
            }.apply()
    }

    companion object {
        private const val SHARED_PREFERENCES_KEY = "MissionHeartPreferencesKey"
    }

}