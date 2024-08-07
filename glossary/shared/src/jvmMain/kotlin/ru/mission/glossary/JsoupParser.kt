package ru.mission.glossary

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.devtools.v124.network.Network
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.TranslateCollectionRoot
import ru.mission.glossary.models.WordTranslateNoId
import ru.mission.glossary.models.WordsDictionary
import java.util.*


internal class JsoupParser : SingleAppParser {


    override suspend fun parse(url: String): DictionaryGetResult {
        var driver: EdgeDriver? = null
        var devTools: DevTools? = null
        try {
            val wordsDictionaryFlow = MutableStateFlow<WordsDictionary?>(null)
            val options = EdgeOptions()
            options.addArguments("--headless")
            driver = EdgeDriver(options)
            devTools = driver.devTools
            devTools.createSession()

            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))

            devTools.addListener(Network.responseReceived()) { responseReceived: ResponseReceived ->
                val responseUrl = responseReceived.response.url
                val requestId = responseReceived.requestId
                if (responseUrl.contains("/props/api/collections")) {
                    val responseBodyText = devTools.send(Network.getResponseBody(requestId)).body
                    val responseModel = Json.decodeFromString<TranslateCollectionRoot>(responseBodyText)

                    val name = responseModel.translateCollection?.name
                    val words = responseModel.translateCollection?.records?.mapNotNull {
                        if (it.text != null && it.translation != null) WordTranslateNoId(it.text, it.translation, null)
                        else null
                    }
                    wordsDictionaryFlow.update { words?.let { WordsDictionary(name ?: "Collection", it) } }
                }
            }


            driver.get(url)
            val wordsDictionary = wordsDictionaryFlow.filterNotNull().first()
            return DictionaryGetResult.Success(wordsDictionary)
        } catch (e: Exception) {
            e.printStackTrace()
            return DictionaryGetResult.Failure(e)
        } finally {
            devTools?.disconnectSession()
            devTools?.close()
            driver?.close()
        }
    }

}