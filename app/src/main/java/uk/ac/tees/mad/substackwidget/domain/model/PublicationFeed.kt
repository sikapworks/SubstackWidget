package uk.ac.tees.mad.substackwidget.domain.model

data class PublicationFeed (
    val publication: Publication,
    val posts: List<Post>
)