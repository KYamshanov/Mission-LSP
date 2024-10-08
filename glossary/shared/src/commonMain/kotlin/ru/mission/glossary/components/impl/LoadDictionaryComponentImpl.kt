package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.LoadSharedCollection
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.LoadDictionaryComponent
import ru.mission.glossary.models.DictionaryGetResult
import java.io.File
import kotlin.coroutines.CoroutineContext

internal class LoadDictionaryComponentImpl(
    componentContext: ComponentContext,
    initialUrl: String,
    mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val singleAppParser: SingleAppParser,
    private val onLoadDictionary: (Long) -> Unit,
    private val back: () -> Unit,
    private val dictionary: Dictionary,
    private val loadSharedCollection: LoadSharedCollection,
) : LoadDictionaryComponent, ComponentContext by componentContext {

    private val _model = MutableValue(LoadDictionaryComponent.Model(
        url = initialUrl,
        filePath = "",
        newCollectionTitle = ""
    ))
    override val model: Value<LoadDictionaryComponent.Model> = _model

    private val scope = coroutineScope(mainContext + SupervisorJob())

    override fun onSetUrl(url: String) {
        _model.update { it.copy(url = url) }
    }

    override fun onSetFilePath(absoluteFilePath: String) {
        _model.update { it.copy(filePath = absoluteFilePath) }
    }

    override fun onClickLoadDictionary() {
        scope.launch {
            val url = _model.value.url
            val result = withContext(defaultContext) {
                val securityUrl = when {
                    !url.startsWith("https") && url.startsWith("http") -> {
                        url.replace("http", "https")
                    }

                    !url.startsWith("https") -> "https://$url"
                    else -> url
                }
                singleAppParser.parse(securityUrl)
            }
            when (result) {
                is DictionaryGetResult.Failure -> {
                    result.reason.printStackTrace()
                }

                is DictionaryGetResult.Success -> {
                    val dictionary = result.dictionary
                    val collection =
                        this@LoadDictionaryComponentImpl.dictionary.saveCollection(dictionary.name, dictionary.words)
                    onLoadDictionary(collection.id)
                }
            }
        }
    }

    override fun onBack() {
        back()
    }

    override fun onClickLoadDictionaryFromFile() {
        scope.launch {
            loadSharedCollection.load(File(_model.value.filePath))
                .onFailure { it.printStackTrace() }
                .onSuccess {
                    onLoadDictionary(it.id)
                }
        }
    }

    override fun setNewCollectionTitle(title: String) {
        _model.update { it.copy(newCollectionTitle = title) }
    }

    override fun createNewCollection() {
        scope.launch {
            val newCollectionName = _model.value.newCollectionTitle
            dictionary.saveCollection(newCollectionName, emptyList())
        }
    }
}