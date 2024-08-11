package ru.mission.glossary.components

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface ListComponent {

    val stack: Value<ChildStack<*, CardComponent>>

    fun onItemClicked(item: String)
    fun onCardSwiped(index: Int, isSuccess: Boolean)

    fun onBack()

    fun swipeWordAndTranslate(index: Int)
}