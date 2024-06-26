package ru.mission.heart.component.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable
import ru.mission.heart.component.*
import ru.mission.heart.component.RootComponent.Child.LoginErrorChild
import ru.mission.heart.component.RootComponent.Child.MainChild
import ru.mission.heart.component.factory.LoginComponentFactory
import ru.mission.heart.session.*
import ru.mission.heart.session.SessionInteractor
import kotlin.coroutines.CoroutineContext

internal class RootComponentImpl(
    componentContext: ComponentContext,
    sessionInteractor: SessionInteractor,
    mainDispatcher: CoroutineContext,
    private val loginComponentFactory: LoginComponentFactory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val retainedAppInitializer = instanceKeeper.getOrCreate { RetainedAppInitializer(sessionInteractor) }

    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.Splash, // The initial child component is List
            handleBackButton = true, // Automatically pop from the stack on back button presses
            childFactory = ::child,
        )

    private val coroutineScope = CoroutineScope(SupervisorJob() + mainDispatcher)

    init {
        doOnDestroy { coroutineScope.cancel("Component is going to destory") }

        coroutineScope.launch {
            retainedAppInitializer.sharedActions.collect {
                when (it) {
                    is RetainedAppInitializer.Action.NewRootScreen -> navigation.replaceAll(it.screenConfig)
                }
            }
        }
    }

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Main -> MainChild(mainComponent(componentContext))
            is Config.LoginError -> LoginErrorChild(detailsComponent(componentContext, config))
            is Config.Splash -> RootComponent.Child.SplashChild(splashComponent(componentContext, config))
            is Config.Login -> RootComponent.Child.LoginChild(loginComponent(componentContext, config))
        }

    private fun mainComponent(componentContext: ComponentContext): MainComponent =
        MainComponentImpl(
            componentContext = componentContext,
        )

    private fun detailsComponent(componentContext: ComponentContext, config: Config.LoginError): LoginErrorComponent =
        LoginErrorComponentImpl(
            componentContext = componentContext,
        )

    private fun splashComponent(componentContext: ComponentContext, config: Config.Splash): SplashComponent =
        SplashComponentImpl(
            componentContext = componentContext,
        )


    private fun loginComponent(componentContext: ComponentContext, config: Config.Login): LoginComponent =
        loginComponentFactory.create(
            componentContext = componentContext,
            onSussessSingIn = {
                navigation.replaceAll(Config.Main)
            },
            onFailedSingIn = {

            }
        )


    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    @Serializable // kotlinx-serialization plugin must be applied
    private sealed interface Config {

        @Serializable
        data object Main : Config

        @Serializable
        data object Splash : Config

        @Serializable
        data object Login : Config

        @Serializable
        data object LoginError : Config
    }

    private class RetainedAppInitializer(
        private val sessionInteractor: SessionInteractor
    ) : InstanceKeeper.Instance {


        private val _sharedActions = MutableSharedFlow<Action>(replay = 1)
        private val coroutineScope = CoroutineScope(SupervisorJob()) //clear on destory

        val sharedActions = _sharedActions.asSharedFlow()

        init {
            coroutineScope.launch {
                val session = sessionInteractor.state
                    .filter { it !is NotInited }
                    .first()

                when (session) {
                    is Failed -> onFailedSession()
                    is JwtSession -> onJwtSession()
                    NotInited -> throw Exception("Unreachable code")
                }
            }
        }

        override fun onDestroy() {
            coroutineScope.cancel("Component is going to destory")
            super.onDestroy()
        }

        private suspend fun onFailedSession() {
            _sharedActions.emit(Action.NewRootScreen(Config.Login))
        }

        private suspend fun onJwtSession() {
            _sharedActions.emit(Action.NewRootScreen(Config.Main))
        }

        sealed interface Action {

            /**
             * equal push to front
             */
            data class NewRootScreen(
                val screenConfig: Config
            ) : Action
        }
    }
}

