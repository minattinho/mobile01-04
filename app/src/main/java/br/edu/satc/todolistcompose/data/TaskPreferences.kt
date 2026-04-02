package br.edu.satc.todolistcompose.data

import android.content.Context

class TaskPreferences(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun isSampleDataSeeded(): Boolean =
        sharedPreferences.getBoolean(KEY_SAMPLE_DATA_SEEDED, false)

    fun setSampleDataSeeded(isSeeded: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_SAMPLE_DATA_SEEDED, isSeeded)
            .apply()
    }

    private companion object {
        private const val PREFERENCES_NAME = "task_preferences"
        private const val KEY_SAMPLE_DATA_SEEDED = "sample_data_seeded"
    }
}
