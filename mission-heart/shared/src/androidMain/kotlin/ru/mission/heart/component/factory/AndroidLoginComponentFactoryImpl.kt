package ru.mission.heart.component.factory

import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.mission.heart.component.LoginComponent
import ru.mission.heart.component.impl.AndroidLoginComponentImpl

internal class AndroidLoginComponentFactoryImpl : LoginComponentFactory, KoinComponent {

    override fun create(componentContext: ComponentContext): LoginComponent =
        AndroidLoginComponentImpl(componentContext, get(), get())
}