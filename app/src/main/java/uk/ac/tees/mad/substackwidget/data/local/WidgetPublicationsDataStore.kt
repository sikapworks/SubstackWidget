package uk.ac.tees.mad.substackwidget.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.substackwidget.domain.model.Publication
import javax.inject.Inject

private val Context.widgetDataStore by preferencesDataStore(name = "widget_publications")
private val PUBLICATIONS_KEY = stringPreferencesKey("publications")

class WidgetPublicationsDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun getPublications(): List<Publication> {
        val prefs = context.widgetDataStore.data.first()
        val json = prefs[PUBLICATIONS_KEY] ?: return emptyList()
        return try {
            Json.decodeFromString(json)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun savePublications(publications: List<Publication>) {
        context.widgetDataStore.edit { prefs ->
            prefs[PUBLICATIONS_KEY] = Json.encodeToString(publications)
        }
    }

    suspend fun removePublication(handle: String) {
        savePublications(getPublications().filterNot { it.handle == handle })
    }
}