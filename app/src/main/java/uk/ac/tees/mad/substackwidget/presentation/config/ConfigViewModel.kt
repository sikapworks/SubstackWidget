package uk.ac.tees.mad.substackwidget.presentation.config

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.substackwidget.domain.usecase.AddPublicationResult
import uk.ac.tees.mad.substackwidget.domain.usecase.ManagePublicationsUseCase
import uk.ac.tees.mad.substackwidget.widget.SubstackWidget
import javax.inject.Inject

@HiltViewModel
class ConfigViewModel @Inject constructor(
    private val managePublicationsUseCase: ManagePublicationsUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState.asStateFlow()

    init { loadSaved() }

    private fun loadSaved() {
        viewModelScope.launch {
            val saved = managePublicationsUseCase.getAll()
            _uiState.update { it.copy(publications = saved, isLoadingSaved = false) }
        }
    }

    fun onInputChange(text: String) {
        _uiState.update { it.copy(inputText = text, errorMessage = null) }
    }

    fun addPublication() {
        val handle = _uiState.value.inputText
        if (_uiState.value.isChecking) return

        viewModelScope.launch {
            _uiState.update { it.copy(isChecking = true, errorMessage = null) }
            when (val result = managePublicationsUseCase.add(handle)) {
                is AddPublicationResult.Success -> _uiState.update {
                    it.copy(
                        publications = it.publications + result.publication,
                        inputText = "",
                        isChecking = false
                    )
                }

                AddPublicationResult.NotFound -> _uiState.update {
                    it.copy(
                        isChecking = false,
                        errorMessage = "Couldn't find that publication — check the spelling"
                    )
                }

                AddPublicationResult.AlreadyAdded -> _uiState.update {
                    it.copy(isChecking = false, errorMessage = "Already added")
                }

                AddPublicationResult.Empty -> _uiState.update {
                    it.copy(isChecking = false, errorMessage = "Type a publication handle first")
                }
            }
        }
    }

    fun removePublication(handle: String) {
        viewModelScope.launch {
            managePublicationsUseCase.remove(handle)
            _uiState.update { state -> state.copy(publications = state.publications.filterNot { it.handle == handle }) }
        }
    }

    private fun refreshWidgets() {
        viewModelScope.launch {
            SubstackWidget().updateAll(context)
        }
    }
}