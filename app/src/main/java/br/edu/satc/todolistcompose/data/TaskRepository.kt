package br.edu.satc.todolistcompose.data

import br.edu.satc.todolistcompose.data.local.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao,
    private val preferences: TaskPreferences
) {
    val tasks: Flow<List<TaskData>> = taskDao.getAllTasks()

    suspend fun addTask(task: TaskData) {
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: TaskData) {
        taskDao.updateTask(task)
    }

    suspend fun seedSampleTasksIfNeeded(sampleTasks: List<TaskData>) {
        val hasTasks = taskDao.getTaskCount() > 0
        val isSeeded = preferences.isSampleDataSeeded()

        if (!hasTasks && !isSeeded) {
            taskDao.insertTasks(sampleTasks)
            preferences.setSampleDataSeeded(true)
            return
        }

        if (hasTasks && !isSeeded) {
            preferences.setSampleDataSeeded(true)
        }
    }
}
