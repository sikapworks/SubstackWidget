package uk.ac.tees.mad.substackwidget.widget

import androidx.compose.ui.graphics.Color
import androidx.glance.color.ColorProvider

object WidgetColors {
    val Background = ColorProvider(day = Color(0xFFFFF8F0), night = Color(0xFF1C1B19))
    val Surface = ColorProvider(day = Color(0xFFFFF8F0), night = Color(0xFF262421))
    val Accent = ColorProvider(day = Color(0xFFFF6719), night = Color(0xFFFF8A4C))
    val TitleText = ColorProvider(day = Color(0xFF1A1A1A), night = Color(0xFFF2ECE6))
    val SubtleText = ColorProvider(day = Color(0xFF888888), night = Color(0xFFA8A29B))
}