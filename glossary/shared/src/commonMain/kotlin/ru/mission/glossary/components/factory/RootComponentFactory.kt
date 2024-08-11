package ru.mission.glossary.components.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.glossary.Dictionary
import ru.mission.glossary.LoadSharedCollection
import ru.mission.glossary.ShareCollection
import ru.mission.glossary.components.impl.DefaultRootComponent
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.RootComponent
import kotlin.coroutines.CoroutineContext

interface RootComponentFactory {

    fun create(componentContext: ComponentContext): RootComponent
}

internal class RootComponentFactoryImpl(
    private val singleAppParser: SingleAppParser,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val dictionary: Dictionary,
    private val shareCollection: ShareCollection,
    private val loadSharedCollection: LoadSharedCollection
) : RootComponentFactory {

    override fun create(componentContext: ComponentContext): RootComponent = DefaultRootComponent(
        componentContext = componentContext,
        singleAppParser = singleAppParser,
        mainContext = mainContext,
        defaultContext = defaultContext,
        dictionary = dictionary,
        shareCollection = shareCollection,
        loadSharedCollection = loadSharedCollection
    )
}