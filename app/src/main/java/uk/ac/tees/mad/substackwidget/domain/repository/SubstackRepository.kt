package uk.ac.tees.mad.substackwidget.domain.repository

import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.domain.model.Publication

interface SubstackRepository {

    suspend fun fetchPosts(publication: Publication, limit: Int): List<Post>

    suspend fun resolveDisplayName(handle: String): String?

    suspend fun getSavedPublications(): List<Publication>

    suspend fun savePublications(publications: List<Publication>)

    suspend fun removePublication(handle: String)
}