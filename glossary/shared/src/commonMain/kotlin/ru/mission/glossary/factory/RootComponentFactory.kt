package ru.mission.glossary.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.glossary.DefaultRootComponent
import ru.mission.glossary.RootComponent
import ru.mission.glossary.SingleAppParser

class RootComponentFactory(
    private val singleAppParser: SingleAppParser,
) {

    fun create(componentContext: ComponentContext): RootComponent = DefaultRootComponent(
        componentContext, singleAppParser
    )
}