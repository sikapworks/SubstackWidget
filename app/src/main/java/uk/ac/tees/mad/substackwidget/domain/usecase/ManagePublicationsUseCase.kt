package uk.ac.tees.mad.substackwidget.domain.usecase

import javax.inject.Inject
import uk.ac.tees.mad.substackwidget.domain.model.Publication
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository

sealed class AddPublicationResult {
    data class Success(val publication: Publication) : AddPublicationResult()
    data object NotFound : AddPublicationResult()
    data object AlreadyAdded : AddPublicationResult()
    data object Empty : AddPublicationResult()
}

class ManagePublicationsUseCase @Inject constructor(
    private val repository: SubstackRepository
) {
    suspend fun add(widgetId: Int, handle: String): AddPublicationResult {
        val trimmed = handle.trim().lowercase()
        if (trimmed.isBlank()) return AddPublicationResult.Empty

        val current = repository.getSavedPublications(widgetId)
        if (current.any { it.handle == trimmed }) return AddPublicationResult.AlreadyAdded

        val resolvedName = repository.resolveDisplayName(trimmed)
            ?: return AddPublicationResult.NotFound

        val publication = Publication(handle = trimmed, displayName = resolvedName)
        repository.savePublications(widgetId, current + publication)
        return AddPublicationResult.Success(publication)
    }

    suspend fun remove(widgetId: Int, handle: String) {
        repository.removePublication(widgetId, handle)
    }

    suspend fun getAll(widgetId: Int): List<Publication> {
        return repository.getSavedPublications(widgetId)
    }
}