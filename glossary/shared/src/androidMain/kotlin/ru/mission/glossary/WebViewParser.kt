package ru.mission.glossary

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.TranslateCollectionRoot
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordsDictionary

internal class WebViewParser(
    private val androidSpaViewModel: AndroidSpaViewModel,
    private val webViewCallback: WebViewCallback,
) : SingleAppParser {

    override suspend fun parse(url: String): DictionaryGetResult {
        val sharedFlow = webViewCallback.sharedFlow
        androidSpaViewModel.loadUrl(url)
        val page = withTimeout(15_000L) {
            sharedFlow
                .filterIsInstance<WebViewCallback.CallbackModel.BodyReceived>()
                .first()
        }
        val responseModel = Json.decodeFromString<TranslateCollectionRoot>(page.body)

        val name = responseModel.translateCollection?.name
        val words = responseModel.translateCollection?.records?.mapNotNull {
            if (it.text != null && it.translation != null)
                WordTranslate(it.text, it.translation) else
                null
        }
        return DictionaryGetResult.Success(WordsDictionary(name ?: "Collection", words!!))
    }
}