package uk.ac.tees.mad.substackwidget.data.remote

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader

data class RawFeedItem(
    val title: String?,
    val link: String?,
    val pubDate: String?,
    val description: String?
)

data class RawFeed(
    val channelTitle: String?,
    val items: List<RawFeedItem>
)

object RssFeedParser {
    fun parse(xml: String): RawFeed {
        val parser = Xml.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        parser.setInput(StringReader(xml))

        var channelTitle: String? = null
        var channelTitleCaptured = false
        val items = mutableListOf<RawFeedItem>()

        var insideItem = false
        var title: String? = null
        var link: String? = null
        var pubDate: String? = null
        var description: String? = null

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "item" -> {
                        insideItem = true
                        title = null; link = null; pubDate = null; description = null
                    }

                    "title" -> if (insideItem) {
                        title = parser.nextText()
                    } else if (!channelTitleCaptured) {
                        channelTitle = parser.nextText()
                        channelTitleCaptured = true
                    }

                    "link" -> if (insideItem) link = parser.nextText()
                    "pubDate" -> if (insideItem) pubDate = parser.nextText()
                    "description" -> if (insideItem) description = parser.nextText()
                }

                XmlPullParser.END_TAG -> if (parser.name == "item") {
                    items.add(RawFeedItem(title, link, pubDate, description))
                    insideItem = false
                }
            }
            eventType = parser.next()
        }

        return RawFeed(channelTitle, items)
    }
}