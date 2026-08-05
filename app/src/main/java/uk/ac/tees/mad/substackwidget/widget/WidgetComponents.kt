package uk.ac.tees.mad.substackwidget.widget

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import uk.ac.tees.mad.substackwidget.domain.model.Post
import uk.ac.tees.mad.substackwidget.formatDate

@Composable
fun SectionHeaderView(title: String) {
    Column(modifier = GlanceModifier.fillMaxWidth().padding(top = 10.dp, bottom = 4.dp)) {
        Text(
            text = title,
            style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = WidgetColors.Accent
            )
        )
    }
}

@Composable
fun PostRowView(post: Post) {
    Column(
        modifier = GlanceModifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(
                actionStartActivity(Intent(Intent.ACTION_VIEW, Uri.parse(post.link)))
            )
    ) {
        Text(
            text = post.title,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = WidgetColors.TitleText
            )
        )
        if (post.snippet.isNotBlank()) {
            Text(
                text = post.snippet,
                maxLines = 2,
                style = TextStyle(fontSize = 12.sp, color = WidgetColors.SubtleText)
            )
        }
        Text(
            text = formatDate(post.pubDate),
            style = TextStyle(fontSize = 11.sp, color = WidgetColors.SubtleText)
        )
    }
}

@Composable
fun EmptyState(message: String, clickIntent: android.content.Intent?) {
    var modifier = GlanceModifier.fillMaxSize().padding(16.dp)
    if (clickIntent != null) {
        modifier = modifier.clickable(actionStartActivity(clickIntent))
    }
    Column(modifier = modifier) {
        Text(text = message, style = TextStyle(fontSize = 13.sp, color = WidgetColors.SubtleText))
    }
}