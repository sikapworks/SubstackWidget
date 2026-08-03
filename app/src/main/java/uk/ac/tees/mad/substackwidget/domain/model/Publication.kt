package uk.ac.tees.mad.substackwidget.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Publication(
    val handle: String,
    val displayName: String
) {
    val feedUrl: String
        get() = "https://${handle}.substack.com/feed"
}