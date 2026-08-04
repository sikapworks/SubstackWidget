package uk.ac.tees.mad.substackwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigScreen
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigViewModel
import uk.ac.tees.mad.substackwidget.ui.theme.SubstackWidgetTheme

@AndroidEntryPoint
class WidgetConfigActivity : ComponentActivity() {

    private val viewModel: ConfigViewModel by viewModels()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If opened from the launcher (not widget config flow), there's no real widgetId —
        // that's fine, use a stable fallback so the app screen still works standalone.
        val effectiveId = if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) 0 else appWidgetId
        viewModel.init(effectiveId)

        setContent {
            SubstackWidgetTheme {
                ConfigScreen(
                    viewModel = viewModel,
                    onDone = {
                        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                            setResult(Activity.RESULT_OK, resultValue)
                        }
                        finish()
                    }
                )
            }
        }
    }
}