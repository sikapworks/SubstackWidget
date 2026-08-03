package uk.ac.tees.mad.substackwidget.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.substackwidget.domain.model.Publication

private val Context.widgetDataStore by preferencesDataStore(name = "widget_publications")

class WidgetPublicationsDataStore(private val context: Context) {

    private fun keyFor(widgetId: Int) = stringPreferencesKey("publications_$widgetId")

    suspend fun getPublications(widgetId: Int): List<Publication> {
        val prefs = context.widgetDataStore.data.first()
        val json = prefs[keyFor(widgetId)] ?: return emptyList()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun savePublications(widgetId: Int, publications: List<Publication>) {
        context.widgetDataStore.edit { prefs ->
            prefs[keyFor(widgetId)] = Json.encodeToString(publications)
        }
    }

    suspend fun removePublication(widgetId: Int, handle: String) {
        val updated = getPublications(widgetId).filterNot { it.handle == handle }
        savePublications(widgetId, updated)
    }
}