package ru.mission.heart.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.heart.component.MainComponent

class MainComponentImpl(
    componentContext: ComponentContext,
) : MainComponent, ComponentContext by componentContext {

    private val _model = MutableValue(MainComponent.Model(
        title = "Welcome to Mission!"
    ))

    override val model: Value<MainComponent.Model> = _model
}