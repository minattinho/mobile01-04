package br.edu.satc.todolistcompose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import br.edu.satc.todolistcompose.data.TaskData
import br.edu.satc.todolistcompose.data.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    val tasks: StateFlow<List<TaskData>> = repository.tasks.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            repository.seedSampleTasksIfNeeded(sampleTasks)
        }
    }

    fun addTask(title: String, description: String) {
        val normalizedTitle = title.trim()
        if (normalizedTitle.isBlank()) return

        viewModelScope.launch {
            repository.addTask(
                TaskData(
                    title = normalizedTitle,
                    description = description.trim(),
                    complete = false
                )
            )
        }
    }

    fun updateTaskCompletion(task: TaskData, isChecked: Boolean) {
        viewModelScope.launch {
            repository.updateTask(task.copy(complete = isChecked))
        }
    }

    private companion object {
        private val sampleTasks = listOf(
            TaskData(
                title = "Comprar pao",
                description = "Comprar pao na padaria",
                complete = false
            ),
            TaskData(
                title = "Estudar Kotlin",
                description = "Estudar Kotlin para o curso de Android",
                complete = true
            ),
            TaskData(
                title = "Ler um livro",
                description = "Ler o livro Clean Code",
                complete = false
            ),
            TaskData(
                title = "Fazer exercicios",
                description = "Fazer exercicios fisicos por 30 minutos",
                complete = true
            ),
            TaskData(
                title = "Assistir serie",
                description = "Assistir a serie Stranger Things",
                complete = false
            ),
            TaskData(
                title = "Cozinhar",
                description = "Cozinhar o jantar para a familia",
                complete = false
            )
        )
    }
}

class TaskViewModelFactory(
    private val repository: TaskRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
