package uk.ac.tees.mad.substackwidget.domain.usecase

import uk.ac.tees.mad.substackwidget.domain.model.Publication
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository

class ManagePublicationsUseCase(
    private val repository: SubstackRepository
) {
    suspend fun add(widgetId: Int, publication: Publication) {
        val current = repository.getSavedPublications(widgetId)
        val updated = current + publication
        if (current.any { it.handle == publication.handle })
            return repository.savePublications(widgetId, updated)
    }

    suspend fun remove(widgetId: Int, handle: String) {
        repository.removePublication(widgetId, handle)
    }
    suspend fun getAll(widgetId: Int): List<Publication> {
        return repository.getSavedPublications(widgetId)
    }
}