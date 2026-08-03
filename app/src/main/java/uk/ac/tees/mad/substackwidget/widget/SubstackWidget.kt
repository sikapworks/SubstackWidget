package uk.ac.tees.mad.substackwidget.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import uk.ac.tees.mad.substackwidget.di.AppContainer

class SubstackWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(id)
        val appContainer = AppContainer(context.applicationContext)

        val groupedFeed = appContainer.getGroupedFeedUseCase(appWidgetId)
        val rows = groupedFeed.toFeedRows()
        val hasAnyPublications = groupedFeed.isNotEmpty()

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF8F0))
                    .cornerRadius(16.dp)
                    .padding(12.dp)
            ) {
                when {
                    !hasAnyPublications -> EmptyState(
                        message = "Tap this widget to add a publication"
                    )

                    rows.isEmpty() -> EmptyState(
                        message = "No posts found — check back soon"
                    )

                    else -> {
                        LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                            items(rows, itemId = { rowId(it) }) { row ->
                                when (row) {
                                    is FeedRow.SectionHeader -> SectionHeaderView(row.title)
                                    is FeedRow.PostRow -> PostRowView(row.post)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun rowId(row: FeedRow): Long = when (row) {
    is FeedRow.SectionHeader -> row.title.hashCode().toLong()
    is FeedRow.PostRow -> row.post.link.hashCode().toLong()
}