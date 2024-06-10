package ru.mission.heart.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    // It's possible to pop multiple screens at a time on iOS
    fun onBackClicked(toIndex: Int)

    // Defines all possible child components
    sealed class Child {
        class MainChild(val component: MainComponent) : Child()
        class LoginErrorChild(val component: LoginErrorComponent) : Child()
        class SplashChild(val component: SplashComponent) : Child()
        class LoginChild(val component: LoginComponent) : Child()
    }
}
