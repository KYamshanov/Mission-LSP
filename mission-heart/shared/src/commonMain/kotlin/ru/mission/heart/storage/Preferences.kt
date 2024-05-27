package ru.mission.heart.storage

internal interface Preferences {


    fun setValue(key: String, value: String)

    /**
     * return null if value did not set
     */
    fun getValue(key: String): String?
}