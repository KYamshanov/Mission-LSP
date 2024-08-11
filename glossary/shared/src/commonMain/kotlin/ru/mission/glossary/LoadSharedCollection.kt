package ru.mission.glossary

import ru.mission.glossary.models.Collection
import java.io.File

internal interface LoadSharedCollection {

    suspend fun load(file: File): Result<Collection>
}