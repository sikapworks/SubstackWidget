package uk.ac.tees.mad.substackwidget

import com.prof18.rssparser.RssParser

class SubstackRepository {
    private val parser = RssParser()

    suspend fun fetchLatestPosts(feedUrl: String, limit: Int = 5): List<SubstackPost> {
        return try {
            val channel = parser.getRssChannel(feedUrl)
            channel.items
                .take(limit)
                .map { item ->
                    SubstackPost(
                        title = item.title ?: "Untitled",
                        link = item.link ?: "",
                        pubDate = item.pubDate ?: ""
                    )
                }

        } catch (e: Exception) {
            emptyList()
        }
    }
}