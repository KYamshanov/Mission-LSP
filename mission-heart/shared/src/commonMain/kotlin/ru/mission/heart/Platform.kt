package ru.mission.heart

import ru.mission.heart.api.Generator
import ru.mission.heart.storage.Preferences

internal expect fun preferences(): Preferences

internal expect fun generator(): Generator

