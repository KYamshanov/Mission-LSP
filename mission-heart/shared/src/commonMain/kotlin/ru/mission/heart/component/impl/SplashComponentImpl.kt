package ru.mission.heart.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.heart.SplashComponent

class SplashComponentImpl(
    componentContext: ComponentContext
) : SplashComponent, ComponentContext by componentContext {

    private val _model = MutableValue(SplashComponent.Model(title = "Test"))
    override val model: Value<SplashComponent.Model> = _model

}