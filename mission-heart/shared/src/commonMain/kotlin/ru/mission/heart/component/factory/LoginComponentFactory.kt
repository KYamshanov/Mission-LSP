package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import ru.mission.heart.component.LoginComponent

internal interface LoginComponentFactory {

    fun create(componentContext: ComponentContext): LoginComponent
}