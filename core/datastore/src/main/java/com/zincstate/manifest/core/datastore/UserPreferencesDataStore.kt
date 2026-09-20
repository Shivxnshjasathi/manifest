package com.zincstate.manifest.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "manifest_preferences")

@Singleton
class UserPreferencesDataStore @Inject constructor(
    private val context: Context
) {
    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val IS_BIOMETRIC_LOCKED = booleanPreferencesKey("is_biometric_locked")
        val IS_AMOUNT_VISIBLE = booleanPreferencesKey("is_amount_visible")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        val LAST_VIEWED_MONTH = stringPreferencesKey("last_viewed_month")
        val IS_DAILY_REMINDER_ENABLED = booleanPreferencesKey("is_daily_reminder_enabled")
        val REMINDER_TIME = stringPreferencesKey("reminder_time") // "HH:mm"
        val CURRENCY_SYMBOL = stringPreferencesKey("currency_symbol")
    }

    val isDarkTheme: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.IS_DARK_THEME] ?: true // Dark mode is default
    }

    val isBiometricLocked: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.IS_BIOMETRIC_LOCKED] ?: false
    }

    val isAmountVisible: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.IS_AMOUNT_VISIBLE] ?: true
    }

    val dynamicColor: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.DYNAMIC_COLOR] ?: true
    }

    val lastViewedMonth: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.LAST_VIEWED_MONTH] ?: ""
    }

    val isDailyReminderEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.IS_DAILY_REMINDER_ENABLED] ?: false
    }

    val reminderTime: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.REMINDER_TIME] ?: "21:00"
    }

    val currencySymbol: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PreferencesKeys.CURRENCY_SYMBOL] ?: "₹"
    }

    suspend fun setDarkTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_DARK_THEME] = isDark
        }
    }

    suspend fun setBiometricLocked(isLocked: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_BIOMETRIC_LOCKED] = isLocked
        }
    }

    suspend fun setAmountVisible(isVisible: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_AMOUNT_VISIBLE] = isVisible
        }
    }

    suspend fun setDynamicColor(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.DYNAMIC_COLOR] = enabled
        }
    }

    suspend fun setLastViewedMonth(yearMonth: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.LAST_VIEWED_MONTH] = yearMonth
        }
    }

    suspend fun setDailyReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.IS_DAILY_REMINDER_ENABLED] = enabled
        }
    }

    suspend fun setReminderTime(time: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.REMINDER_TIME] = time
        }
    }

    suspend fun setCurrencySymbol(symbol: String) {
        context.dataStore.edit { prefs ->
            prefs[PreferencesKeys.CURRENCY_SYMBOL] = symbol
        }
    }

    suspend fun toggleDarkTheme() {
        context.dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.IS_DARK_THEME] ?: true
            prefs[PreferencesKeys.IS_DARK_THEME] = !current
        }
    }

    suspend fun toggleDynamicColor() {
        context.dataStore.edit { prefs ->
            val current = prefs[PreferencesKeys.DYNAMIC_COLOR] ?: true
            prefs[PreferencesKeys.DYNAMIC_COLOR] = !current
        }
    }
}
