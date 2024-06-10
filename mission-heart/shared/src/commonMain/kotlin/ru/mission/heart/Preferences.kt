package ru.mission.heart

internal interface Preferences {

    /**
     * return null if value did not set
     */
    fun getValue(key: String): String?
    fun remove(key: String)
    fun saveValue(key: String, value: String)
}