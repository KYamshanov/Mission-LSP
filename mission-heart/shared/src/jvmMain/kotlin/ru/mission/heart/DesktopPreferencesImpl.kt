package ru.mission.heart


internal class DesktopPreferencesImpl : Preferences {

    private val preferences = java.util.prefs.Preferences.userRoot()

    override fun saveValue(key: String, value: String) {
        preferences.put(key, value)
    }

    override fun getValue(key: String): String? = preferences.get(/* key = */ key, /* def = */ null)

    override fun remove(key: String) {
        preferences.remove(key)
    }
}