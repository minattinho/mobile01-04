package br.edu.satc.todolistcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import br.edu.satc.todolistcompose.data.TaskPreferences
import br.edu.satc.todolistcompose.data.TaskRepository
import br.edu.satc.todolistcompose.data.local.TaskDatabase
import br.edu.satc.todolistcompose.ui.screens.HomeScreen
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme
import br.edu.satc.todolistcompose.ui.viewmodel.TaskViewModel
import br.edu.satc.todolistcompose.ui.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels {
        TaskViewModelFactory(
            repository = TaskRepository(
                taskDao = TaskDatabase.getDatabase(applicationContext).taskDao(),
                preferences = TaskPreferences(applicationContext)
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val tasks by taskViewModel.tasks.collectAsState()

            ToDoListComposeTheme {
                HomeScreen(
                    tasks = tasks,
                    onAddTask = taskViewModel::addTask,
                    onTaskCheckedChange = taskViewModel::updateTaskCompletion
                )
            }
        }
    }
}
