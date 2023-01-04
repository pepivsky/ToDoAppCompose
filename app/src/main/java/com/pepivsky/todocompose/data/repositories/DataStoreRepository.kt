package com.pepivsky.todocompose.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pepivsky.todocompose.data.models.Priority
import com.pepivsky.todocompose.util.Constants.PREFERENCE_KEY
import com.pepivsky.todocompose.util.Constants.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// kotlin extension property para acceder a dataStore
private val Context.dataStore by preferencesDataStore(
    name = PREFERENCE_NAME
)

// anotacion necesaria para inyectar esta clase dentro del viewModel
@ViewModelScoped
class DataStoreRepository @Inject constructor(
    // inyectando el context con hilt
    @ApplicationContext private val context: Context
) {

    private object PreferenceKeys {
        val sortKey = stringPreferencesKey(name = PREFERENCE_KEY)
    }

    private val dataStore = context.dataStore

    // save value
    suspend fun persistSrotState(priority: Priority) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.sortKey] = priority.name
        }
    }

    // read value
    val readSortState: Flow<String> = dataStore
        .data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        // sino hay nada guardado entonces al leer regresa el valor priority.NONE
        .map { preferences ->
            val sortState = preferences[PreferenceKeys.sortKey] ?: Priority.NONE.name
            sortState
        }
}