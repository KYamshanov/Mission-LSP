package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import ru.mission.glossary.Dictionary
import ru.mission.glossary.ShareCollection
import ru.mission.glossary.components.CollectionsComponent
import ru.mission.glossary.models.Collection
import ru.mission.glossary.models.WordsDictionary
import kotlin.coroutines.CoroutineContext

internal class CollectionsComponentImpl(
    componentContext: ComponentContext,
    mainContext: CoroutineContext,
    private val shareCollection: ShareCollection,
    private val dictionary: Dictionary,
    private val loadCollection: () -> Unit,
    private val openCollection: (Long) -> Unit,
    private val openCollectionWithRefresh: (Long) -> Unit
) : ComponentContext by componentContext, CollectionsComponent {

    private val _model: MutableValue<CollectionsComponent.Model> = MutableValue(CollectionsComponent.Loading)

    override val model: Value<CollectionsComponent.Model>
        get() = _model

    override fun clickOnCollection(collection: Collection) {
        openCollection(collection.id)
    }

    override fun shareCollection(collection: Collection) {
        scope.launch {
            shareCollection.share(collection, dictionary.getWords(collection.id))
                .onFailure { it.printStackTrace() }
        }
    }

    private val scope = coroutineScope(mainContext + SupervisorJob())

    init {
        scope.launch {
            val collections = dictionary.getCollections()
            _model.update { CollectionsComponent.Done(collections) }
        }
    }

    override fun loadNewCollection() = loadCollection()
}