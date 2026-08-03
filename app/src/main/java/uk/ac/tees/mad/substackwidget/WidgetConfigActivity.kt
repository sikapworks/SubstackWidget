package uk.ac.tees.mad.substackwidget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.substackwidget.di.AppContainer
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigScreen
import uk.ac.tees.mad.substackwidget.presentation.config.ConfigViewModelFactory

class WidgetConfigActivity : ComponentActivity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(Activity.RESULT_CANCELED)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        val appContainer = AppContainer(applicationContext)

        setContent {
            val viewModel: uk.ac.tees.mad.substackwidget.presentation.config.ConfigViewModel =
                viewModel(
                    factory = ConfigViewModelFactory(
                        appWidgetId,
                        appContainer.managePublicationsUseCase
                    )
                )

            ConfigScreen(
                viewModel = viewModel,
                onDone = {
                    val resultValue =
                        Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    setResult(Activity.RESULT_OK, resultValue)
                    finish()
                }
            )
        }
    }
}