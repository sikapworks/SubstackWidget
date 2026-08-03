package uk.ac.tees.mad.substackwidget

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

class SubstackWidget : GlanceAppWidget() {
    private val repository = SubstackRepository()

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        val feedUrl = "https://<your-publication>.substack.com/feed"
        val posts = repository.fetchLatestPosts(feedUrl, limit = 5)
        provideContent {
            Column(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .background(Color(0xFFFFF8F0))
                    .cornerRadius(16.dp)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Latest from your newsletter",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = ColorProvider(Color(0xFFFF6719))
                    )
                )
                Spacer(modifier = GlanceModifier.height(10.dp))
                if (posts.isEmpty()) {
                    Text(
                        text = "No posts found",
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = ColorProvider(Color.Gray)
                        )
                    )
                } else {
                    posts.forEachIndexed { index, post ->
                        Text(
                            text = post.title,
                            modifier = GlanceModifier.clickable(
                                actionStartActivity(
                                    Intent(Intent.ACTION_VIEW, Uri.parse(post.link))
                                )
                            ),
                            style = TextStyle(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp,
                                color = ColorProvider(Color(0xFF1A1A1A))
                            )
                        )
                        Text(
                            text = formatDate(post.pubDate),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = ColorProvider(Color(0xFF888888))
                            )
                        )
                        if (index != posts.lastIndex) {
                            Spacer(modifier = GlanceModifier.height(8.dp))
                        }
                    }

                }
            }
        }
    }
}