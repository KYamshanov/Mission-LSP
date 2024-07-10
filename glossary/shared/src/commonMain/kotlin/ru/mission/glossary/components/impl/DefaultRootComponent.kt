package ru.mission.glossary.components.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import ru.mission.glossary.Dictionary
import ru.mission.glossary.components.RootComponent.Child.DetailsChild
import ru.mission.glossary.components.RootComponent.Child.ListChild
import ru.mission.glossary.SingleAppParser
import ru.mission.glossary.components.*
import kotlin.coroutines.CoroutineContext

internal class DefaultRootComponent(
    componentContext: ComponentContext,
    private val singleAppParser: SingleAppParser,
    private val mainContext: CoroutineContext,
    private val defaultContext: CoroutineContext,
    private val dictionary: Dictionary,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Collections, // The initial child component is List
            handleBackButton = true, // Automatically pop from the stack on back button presses
            childFactory = ::child,
        )

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.List -> ListChild(listComponent(componentContext, config))
            is Config.Details -> DetailsChild(detailsComponent(componentContext, config))

            is Config.LoadDictionary -> {
                RootComponent.Child.LoadDictionaryChild(
                    loadDictionaryComponent(componentContext, config)
                )
            }

            is Config.Collections -> RootComponent.Child.CollectionsChild(
                collectionsComponent(
                    componentContext,
                    config
                )
            )
        }

    private fun listComponent(componentContext: ComponentContext, config: Config.List): ListComponent =
        DefaultListComponent(
            componentContext = componentContext,
            onItemSelected = { item: String -> // Supply dependencies and callbacks
                navigation.push(Config.Details(item = item)) // Push the details component
            },
            mainContext = mainContext,
            dictionary = dictionary,
            collectionId = config.collectionId,
            back = { navigation.pop() }
        )

    private fun detailsComponent(componentContext: ComponentContext, config: Config.Details): DetailsComponent =
        DefaultDetailsComponent(
            componentContext = componentContext,
            title = config.item, // Supply arguments from the configuration
            onFinished = navigation::pop, // Pop the details component
        )

    private fun loadDictionaryComponent(
        componentContext: ComponentContext,
        config: Config.LoadDictionary,
    ): LoadDictionaryComponent =
        LoadDictionaryComponentImpl(
            componentContext = componentContext,
            initialUrl = config.initialUrl,
            onLoadDictionary = { navigation.replaceCurrent(DefaultRootComponent.Config.List(it)) },
            dictionary = dictionary,
            mainContext = mainContext,
            defaultContext = defaultContext,
            singleAppParser = singleAppParser,
            back = { navigation.pop() }
        )

    private fun collectionsComponent(
        componentContext: ComponentContext,
        config: Config.Collections,
    ): CollectionsComponent =
        CollectionsComponentImpl(
            componentContext = componentContext,
            mainContext = mainContext,
            dictionary = dictionary,
            loadCollection = { navigation.push(Config.LoadDictionary()) },
            openCollection = { navigation.push(Config.List(it)) }
        )

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private sealed interface Config {

        @Serializable
        data class List(val collectionId: Long) : Config

        @Serializable
        data class Details(val item: String) : Config

        @Serializable
        data class LoadDictionary(val initialUrl: String = "") : Config

        @Serializable
        data object Collections : Config
    }
}

