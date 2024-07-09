package ru.mission.glossary

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.TranslateCollectionRoot
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordsDictionary

internal class WebViewParser(
    private val webViewKeeper: WebViewKeeper,
    private val mainContext: CoroutineContext,
) : SingleAppParser {

    override suspend fun parse(url: String): DictionaryGetResult {
        val webView = webViewKeeper.webView
        val onPageLoadedStateFlow = webViewKeeper.onPageLoadedStateFlow
        if (webView == null || onPageLoadedStateFlow == null) {
            return DictionaryGetResult.Failure(IllegalStateException("WebView not set"))
        }
        withContext(mainContext){
            webView.loadUrl(url)
        }
        val page = withTimeout(15_000L) {
            onPageLoadedStateFlow
                .filterNotNull()
                .filter { it.url.contains("/props/api/collections") }
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