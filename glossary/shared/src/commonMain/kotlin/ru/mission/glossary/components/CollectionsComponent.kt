package ru.mission.glossary.components

import com.arkivanov.decompose.value.Value
import ru.mission.glossary.models.Collection

interface CollectionsComponent {
    val model: Value<Model>

    fun clickOnCollection(collection: Collection)

    fun shareCollection(collection: Collection)

    fun loadNewCollection()

    fun editCollection(collection: Collection)

    sealed interface Model {

        data object Loading : Model

        data class Done(
            val collections: List<Collection>,
        ) : Model
    }
}