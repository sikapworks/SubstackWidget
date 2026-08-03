package uk.ac.tees.mad.substackwidget.presentation.config

import uk.ac.tees.mad.substackwidget.domain.model.Publication

data class ConfigUiState(
    val widgetId: Int = -1,
    val publications: List<Publication> = emptyList(),
    val inputText: String = "",
    val isChecking: Boolean = false,
    val errorMessage: String? = null,
    val isLoadingSaved: Boolean = true
)