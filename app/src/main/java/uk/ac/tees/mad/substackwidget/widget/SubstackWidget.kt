package uk.ac.tees.mad.substackwidget.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import dagger.hilt.android.EntryPointAccessors
import uk.ac.tees.mad.substackwidget.WidgetConfigActivity
import uk.ac.tees.mad.substackwidget.di.WidgetEntryPoint

private const val TAG = "SubstackWidget"

class SubstackWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            WidgetEntryPoint::class.java
        )
        val getGroupedFeedUseCase = entryPoint.getGroupedFeedUseCase()

        var rows: List<FeedRow> = emptyList()
        var hasAnyPublications = false
        var loadFailed = false

        try {
            val groupedFeed = getGroupedFeedUseCase()
            rows = groupedFeed.toFeedRows()
            hasAnyPublications = groupedFeed.isNotEmpty()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load grouped feed", e)
            loadFailed = true
        }

        val configIntent = Intent(context, WidgetConfigActivity::class.java).apply {
            putExtra(WidgetConfigActivity.EXTRA_SHOW_CONFIG, true)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxSize()
                    .background(WidgetColors.Background)
                    .cornerRadius(16.dp)
                    .padding(16.dp)
            ) {
                when {
                    loadFailed -> EmptyState("Something went wrong — check your connection", null)
                    !hasAnyPublications -> EmptyState("Tap to add a publication", configIntent)
                    rows.isEmpty() -> EmptyState("No posts found — check back soon", configIntent)
                    else -> LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
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

private fun rowId(row: FeedRow): Long = when (row) {
    is FeedRow.SectionHeader -> row.title.hashCode().toLong()
    is FeedRow.PostRow -> row.post.link.hashCode().toLong()
}