package ru.mission.glossary

import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import org.openqa.selenium.devtools.DevTools
import org.openqa.selenium.devtools.v124.network.Network
import org.openqa.selenium.devtools.v124.network.model.ResponseReceived
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import ru.mission.glossary.models.DictionaryGetResult
import ru.mission.glossary.models.TranslateCollectionRoot
import ru.mission.glossary.models.WordTranslate
import ru.mission.glossary.models.WordsDictionary
import java.util.*


class JsoupParser : SingleAppParser {


    override suspend fun parse(url: String): DictionaryGetResult {
        var driver: EdgeDriver? = null
        var devTools: DevTools? = null
        try {
            var wordsDictionaryFlow = MutableStateFlow<WordsDictionary?>(null)
            val options = EdgeOptions()
            options.addArguments("--headless");
            driver = EdgeDriver(options)
            devTools = driver.devTools
            devTools.createSession();

            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()))

            devTools.addListener(Network.responseReceived()) { responseReceived: ResponseReceived ->
                val responseUrl = responseReceived.response.url
                val requestId = responseReceived.requestId
                if (responseUrl.contains("/props/api/collections")) {
                    val responseBodyText = devTools.send(Network.getResponseBody(requestId)).body
                    val responseModel = Json.decodeFromString<TranslateCollectionRoot>(responseBodyText)
                    println(responseModel.translateCollection?.authorName)

                    val words = responseModel.translateCollection?.records?.mapNotNull {
                        if (it.text != null && it.translation != null)
                            WordTranslate(it.text, it.translation) else
                            null
                    }
                    wordsDictionaryFlow.update { words?.let { WordsDictionary(it) } }
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