package ru.mission.glossary

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.ShareCollectionJsonModel
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordTranslateNoId
import java.io.File

internal class ShareCollectionDesktop : ShareCollection {
    override fun share(collection: Collection, words: List<WordTranslate>): Result<Unit> = runCatching {
        val applicationDataFolder = File(File(System.getenv("APPDATA")), "Glossary")
            .apply { if (!exists()) mkdirs() }
            .let { File(it, "${collection.name}.json") }
        val wordsJson =
            Json.encodeToString(ShareCollectionJsonModel(collection.name, words.map { it.toWordTranslateNoId() }))
        applicationDataFolder.writeText(wordsJson)
        @Suppress("DEPRECATION")
        Runtime.getRuntime().exec("explorer.exe /select," + applicationDataFolder.absolutePath)
    }
}

private fun WordTranslate.toWordTranslateNoId(): WordTranslateNoId =
    WordTranslateNoId(
        word = word, translate = translate, imageUrl = imageUrl
    )
