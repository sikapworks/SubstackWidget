package uk.ac.tees.mad.substackwidget.domain.repository

import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.domain.model.Publication

interface SubstackRepository {

    suspend fun fetchPosts(publication: Publication, limit: Int): List<Post>

    suspend fun validatePublication(handle: String): Boolean

    suspend fun getSavedPublications(widgetId: Int): List<Publication>

    suspend fun savePublications(widgetId: Int, publications: List<Publication>)

    suspend fun removePublication(widgetId: Int, handle: String)
}