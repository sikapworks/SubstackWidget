package uk.ac.tees.mad.substackwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigScreen
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigViewModel
import uk.ac.tees.mad.substackwidget.presentation.config.InstructionsScreen
import uk.ac.tees.mad.substackwidget.ui.theme.SubstackWidgetTheme
import uk.ac.tees.mad.substackwidget.widget.SubstackWidgetReceiver

@AndroidEntryPoint
class WidgetConfigActivity : ComponentActivity() {

    private val viewModel: ConfigViewModel by viewModels()
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private var isConfigureFlow = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)

        isConfigureFlow = intent?.action == AppWidgetManager.ACTION_APPWIDGET_CONFIGURE
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val showConfigDirectly = intent?.getBooleanExtra(EXTRA_SHOW_CONFIG, false) == true
        val shouldShowConfig = isConfigureFlow || showConfigDirectly

        setContent {
            SubstackWidgetTheme {
                if (shouldShowConfig) {
                    ConfigScreen(
                        viewModel = viewModel,
                        onDone = {
                            if (isConfigureFlow && appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                                val result = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                                setResult(Activity.RESULT_OK, result)
                            }
                            finish()
                        }
                    )
                } else {
                    InstructionsScreen(onAddWidgetClick = { requestPinWidget() })
                }
            }
        }
    }

    private fun requestPinWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)
        val provider = ComponentName(this, SubstackWidgetReceiver::class.java)
        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(provider, null, null)
        }
    }

    companion object {
        const val EXTRA_SHOW_CONFIG = "show_config"
    }
}