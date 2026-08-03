package uk.ac.tees.mad.substackwidget.domain.model

data class Publication(
    val handle: String,
    val displayName: String
) {
    val feedUrl: String
        get() = "https://${handle}.substack.com/feed"
}