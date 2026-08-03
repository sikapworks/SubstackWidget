package uk.ac.tees.mad.substackwidget.presentation.config

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uk.ac.tees.mad.substackwidget.domain.usecase.ManagePublicationsUseCase

class ConfigViewModelFactory(
    private val widgetId: Int,
    private val managePublicationsUseCase: ManagePublicationsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ConfigViewModel(widgetId, managePublicationsUseCase) as T
    }
}