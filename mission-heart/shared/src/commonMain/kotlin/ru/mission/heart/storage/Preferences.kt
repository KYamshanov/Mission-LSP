package ru.mission.heart.storage

internal expect fun setValue(key: String, value: String)

/**
 * return null if value did not set
 */
internal expect fun getValue(key: String): String?