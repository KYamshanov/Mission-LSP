package ru.mission.heart.storage

internal interface Preferences {

    /**
     * return null if value did not set
     */
    fun getValue(key: String): String?
    fun remove(key: String)
    fun saveValue(key: String, value: String)
}