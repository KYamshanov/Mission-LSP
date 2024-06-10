package ru.mission.heart.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import ru.mission.heart.component.LoginErrorComponent

internal class LoginErrorComponentImpl(
    componentContext: ComponentContext,
) : LoginErrorComponent, ComponentContext by componentContext {

    private val _model = MutableValue(LoginErrorComponent.Model(title = "Something went wrong!"))

    override val model: Value<LoginErrorComponent.Model> = _model
}

