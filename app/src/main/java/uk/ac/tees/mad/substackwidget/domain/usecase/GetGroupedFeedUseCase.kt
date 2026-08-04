package uk.ac.tees.mad.substackwidget.domain.usecase

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import uk.ac.tees.mad.substackwidget.domain.model.PublicationFeed
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository
import javax.inject.Inject

class GetGroupedFeedUseCase @Inject constructor(
    private val repository: SubstackRepository
) {
    suspend operator fun invoke(
        widgetId: Int,
        postsPerPublication: Int = 5
    ): List<PublicationFeed> {
        val publications = repository.getSavedPublications(widgetId)
        return coroutineScope {
            publications.map { publication ->
                async {
                    val posts = repository.fetchPosts(publication, postsPerPublication)
                    PublicationFeed(publication, posts)
                }
            }.map { it.await() }
        }
    }
}