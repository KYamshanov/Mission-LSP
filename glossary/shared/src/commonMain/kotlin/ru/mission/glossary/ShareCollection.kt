package ru.mission.glossary

import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.WordTranslate

/**
 * utils for share collection
 * at windows it create new file with json
 * at android it uses share intent for share file with json
 */
internal interface ShareCollection {

    fun share(collection: Collection, words: List<WordTranslate>): Result<Unit>
}