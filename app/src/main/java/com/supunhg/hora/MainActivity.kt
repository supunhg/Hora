package com.supunhg.hora

import androidx.lifecycle.viewmodel.compose.viewModel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.supunhg.hora.ui.theme.HoraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HoraTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    HomeScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }

            }
        }
    }
}

enum class TaskStatus {
    PENDING,
    DONE,
    DROPPED
}

data class Task(
    val id: Int,
    val title: String,
    val status: TaskStatus
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = viewModel()
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Today Tasks")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(viewModel.tasks, key = { it.id }) { task ->
                TaskItem(
                    task = task,
                    onToggle = { viewModel.toggleTask(task.id) },
                    onDrop = { viewModel.dropTask(task.id) },
                    onDelete = { viewModel.deleteTask(task.id)}
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDrop: () -> Unit,
    onDelete: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val displayText = when (task.status) {
        TaskStatus.PENDING -> task.title
        TaskStatus.DONE -> "✔ ${task.title}"
        TaskStatus.DROPPED -> "✖ ${task.title}"
    }

    Text(
        text = displayText,
        modifier = Modifier
            .combinedClickable(
                onClick = { onToggle() },
                onLongClick =  { showDialog = true }
            )
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Task Actions") },
            text = { Text("Choose what to do with this task.") },
            confirmButton = {
                Button(onClick = {
                    onDrop()
                    showDialog = false
                }) {
                    Text("Drop")
                }
            },
            dismissButton = {
                Button(onClick = {
                    onDelete()
                    showDialog = false
                }) {
                    Text("Delete")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HoraTheme {
        HomeScreen()
    }
}