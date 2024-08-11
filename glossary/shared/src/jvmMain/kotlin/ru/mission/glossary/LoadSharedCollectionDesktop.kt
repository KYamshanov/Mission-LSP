package ru.mission.glossary

import kotlinx.serialization.json.Json
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.ShareCollectionJsonModel
import java.io.File

internal class LoadSharedCollectionDesktop(
    private val dictionary: Dictionary
) : LoadSharedCollection {

    override suspend fun load(file: File): Result<Collection> = runCatching {
        val shareCollectionJsonModel: ShareCollectionJsonModel = Json.decodeFromString(file.readText())
        dictionary.saveCollection(shareCollectionJsonModel.collectionName, shareCollectionJsonModel.words)
    }
}