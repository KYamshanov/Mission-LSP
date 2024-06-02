package ru.mission.heart.component

import com.arkivanov.decompose.value.Value

interface LoginComponent {

    val model: Value<Model>

    /**
     * Mehtod is called when Browser navigate to https://$domain/$platform/authorized?code=$some_code
     *
     * through it authorization gets finish
     */
    fun onAuthorized(url: String)

    /**
     * @property authorizationUrl Url for authorization login form
     */
    data class Model(
        val authorizationUrl: String,
    )
}