package uk.ac.tees.mad.substackwidget.data.repository

import android.util.Log
import androidx.core.text.HtmlCompat
import uk.ac.tees.mad.substackwidget.data.local.WidgetPublicationsDataStore
import uk.ac.tees.mad.substackwidget.data.remote.RssFeedParser
import uk.ac.tees.mad.substackwidget.data.remote.SubstackApi
import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.domain.model.Publication
import uk.ac.tees.mad.substackwidget.domain.repository.SubstackRepository
import javax.inject.Inject

private const val TAG = "SubstackRepository"

class SubstackRepositoryImpl @Inject constructor(
    private val api: SubstackApi,
    private val localDataSource: WidgetPublicationsDataStore
) : SubstackRepository {

    override suspend fun fetchPosts(publication: Publication, limit: Int): List<Post> {
        return try {
            val xml = api.getFeed(publication.feedUrl)
            val feed = RssFeedParser.parse(xml)
            Log.d(TAG, "Fetched ${feed.items.size} items for ${publication.handle}")
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
            Log.e(TAG, "fetchPosts failed for ${publication.handle}", e)
            emptyList()
        }
    }

    override suspend fun resolveDisplayName(handle: String): String? {
        return try {
            val xml = api.getFeed("https://$handle.substack.com/feed")
            val feed = RssFeedParser.parse(xml)
            feed.channelTitle?.trim()?.takeIf { it.isNotBlank() } ?: handle
        } catch (e: Exception) {
            Log.e(TAG, "resolveDisplayName failed for $handle", e)
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