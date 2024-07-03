package ru.mission.glossary.components.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.glossary.components.impl.DefaultRootComponent
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.RootComponent
import kotlin.coroutines.CoroutineContext

class RootComponentFactory(
    private val singleAppParser: SingleAppParser,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
) {

    fun create(componentContext: ComponentContext): RootComponent = DefaultRootComponent(
        componentContext = componentContext,
        singleAppParser = singleAppParser,
        mainContext = mainContext,
        defaultContext = defaultContext
    )
}