package ru.mission.glossary

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.mission.glossary.AndroidSpaViewModel.Model
import ru.mission.glossary.WebViewCallback.CallbackModel

internal class AndroidSpaViewModelImpl : AndroidSpaViewModel, WebViewCallback {

    private val _sharedFlow = MutableSharedFlow<CallbackModel>(replay = 1)
    override val sharedFlow: SharedFlow<CallbackModel> = _sharedFlow.asSharedFlow()

    private val _modelState = MutableStateFlow<Model>(Model.Inactive)
    override val webViewState: StateFlow<Model> = _modelState.asStateFlow()

    override fun loadUrl(url: String) {
        _modelState.update { Model.LoadUrl(url) }
    }

    override fun onResponseReceived(url: String, body: String) {
        _modelState.update { Model.Inactive }
        _sharedFlow.tryEmit(CallbackModel.BodyReceived(url, body))
    }

    override fun interceptRequest(url: String): Boolean {
        if (url.contains(COLLECTION_URL)) {
            _modelState.update { Model.FetchUrl(url) }
            return true
        }
        return false
    }

    companion object {

        private const val COLLECTION_URL = "/props/api/collections"
    }
}