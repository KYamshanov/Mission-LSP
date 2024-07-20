package ru.mission.glossary.models

import kotlinx.serialization.Serializable
import kotlin.random.Random

/**
 * alpha - [0; 255]. by default 255 it means not transparant
 */
@Serializable
data class ColorRGBA(
    val red: Int,
    val green: Int,
    val blue: Int,
    val alpha: Int = 255
)

fun RandomColor(): ColorRGBA = ColorRGBA(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))