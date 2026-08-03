package uk.ac.tees.mad.substackwidget.data.repository

import androidx.core.text.HtmlCompat
import uk.ac.tees.mad.substackwidget.data.local.WidgetPublicationsDataStore
import uk.ac.tees.mad.substackwidget.data.remote.SubstackApi
import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.domain.model.Publication
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository

class SubstackRepositoryImpl(
    private val api: SubstackApi,
    private val localDataSource: WidgetPublicationsDataStore
) : SubstackRepository {

    override suspend fun fetchPosts(publication: Publication, limit: Int): List<Post> {
        return try {
            val xml = api.getFeed(publication.feedUrl)
            val feed = uk.ac.tees.mad.substackwidget.data.remote.RssFeedParser.parse(xml)

            feed.items.take(limit).map { item ->
                Post(
                    title = item.title?.trim().orEmpty().ifBlank { "Untitled" },
                    link = item.link?.trim().orEmpty(),
                    pubDate = item.pubDate?.trim().orEmpty(),
                    snippet = item.description?.let { stripHtml(it).take(140) }.orEmpty(),
                    publicationHandle = publication.handle
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun resolveDisplayName(handle: String): String? {
        return try {
            val xml = api.getFeed("https://$handle.substack.com/feed")
            val feed = uk.ac.tees.mad.substackwidget.data.remote.RssFeedParser.parse(xml)
            feed.channelTitle?.trim()?.takeIf { it.isNotBlank() } ?: handle
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getSavedPublications(widgetId: Int): List<Publication> =
        localDataSource.getPublications(widgetId)

    override suspend fun savePublications(widgetId: Int, publications: List<Publication>) =
        localDataSource.savePublications(widgetId, publications)

    override suspend fun removePublication(widgetId: Int, handle: String) =
        localDataSource.removePublication(widgetId, handle)

    private fun stripHtml(raw: String): String =
        HtmlCompat.fromHtml(raw, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()
}