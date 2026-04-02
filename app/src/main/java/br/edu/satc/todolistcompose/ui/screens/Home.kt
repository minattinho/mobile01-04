@file:OptIn(ExperimentalMaterial3Api::class)

package br.edu.satc.todolistcompose.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.satc.todolistcompose.data.TaskData
import br.edu.satc.todolistcompose.ui.components.TaskCard
import br.edu.satc.todolistcompose.ui.theme.ToDoListComposeTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    ToDoListComposeTheme {
        HomeScreen(
            tasks = previewTasks,
            onAddTask = { _, _ -> },
            onTaskCheckedChange = { _, _ -> }
        )
    }
}

@Composable
fun HomeScreen(
    tasks: List<TaskData>,
    onAddTask: (title: String, description: String) -> Unit,
    onTaskCheckedChange: (task: TaskData, isChecked: Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp)
    ) {
        Content(
            tasks = tasks,
            onTaskCheckedChange = onTaskCheckedChange
        )

        NewTask(onAddTask = onAddTask)
    }
}

@Composable
fun Content(
    tasks: List<TaskData>,
    onTaskCheckedChange: (task: TaskData, isChecked: Boolean) -> Unit
) {
    if (tasks.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nenhuma tarefa cadastrada ainda.",
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 96.dp)
        ) {
            items(items = tasks, key = { task -> task.id }) { task ->
                TaskCard(
                    taskData = task,
                    onTaskCheckedChange = { isChecked ->
                        onTaskCheckedChange(task, isChecked)
                    }
                )
            }
        }
    }
}

@Composable
fun NewTask(onAddTask: (title: String, description: String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var showBottomSheet by remember { mutableStateOf(false) }
    val isSaveEnabled = taskTitle.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            text = { Text("Nova tarefa") },
            icon = { Icon(Icons.Filled.Add, contentDescription = "") },
            onClick = {
                showBottomSheet = true
            }
        )
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedTextField(
                    value = taskTitle,
                    onValueChange = { taskTitle = it },
                    label = { Text(text = "Titulo da tarefa") }
                )
                OutlinedTextField(
                    value = taskDescription,
                    onValueChange = { taskDescription = it },
                    label = { Text(text = "Descricao da tarefa") }
                )
                Button(
                    modifier = Modifier.padding(top = 4.dp),
                    enabled = isSaveEnabled,
                    onClick = {
                        onAddTask(
                            taskTitle.trim(),
                            taskDescription.trim()
                        )

                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }

                        taskTitle = ""
                        taskDescription = ""
                    }
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}

private val previewTasks = listOf(
    TaskData(
        id = 1,
        title = "Comprar pao",
        description = "Comprar pao na padaria",
        complete = false
    ),
    TaskData(
        id = 2,
        title = "Estudar Kotlin",
        description = "Revisar Room e Jetpack Compose",
        complete = true
    )
)
