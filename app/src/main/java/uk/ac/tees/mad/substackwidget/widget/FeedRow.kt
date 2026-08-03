package uk.ac.tees.mad.substackwidget.widget

import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.domain.model.PublicationFeed

sealed class FeedRow {
    data class SectionHeader(val title: String) : FeedRow()
    data class PostRow(val post: Post) : FeedRow()
}

fun List<PublicationFeed>.toFeedRows(): List<FeedRow> {
    return flatMap { feed ->
        if (feed.posts.isEmpty()) {
            emptyList()
        } else {
            listOf(FeedRow.SectionHeader(feed.publication.displayName)) +
                    feed.posts.map { FeedRow.PostRow(it) }
        }
    }
}