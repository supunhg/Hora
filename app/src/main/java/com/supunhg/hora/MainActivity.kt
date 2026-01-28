package com.supunhg.hora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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
fun HomeScreen(modifier: Modifier = Modifier) {

    var tasks by remember {
        mutableStateOf(
            listOf(
                Task(1, "Buy Groceries", TaskStatus.PENDING),
                Task(2, "Study Kotlin", TaskStatus.PENDING),
                Task(3, "Workout", TaskStatus.PENDING),
            )
        )
    }

    Column(modifier = modifier) {
        Text("Today Tasks")

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(
                items = tasks,
                key = { it.id }
            ) { task ->
                TaskItem(
                    task = task,
                    onToggle = {
                        tasks = tasks.map { current ->
                            if (current.id == task.id) {
                                current.copy(
                                    status = if (current.status == TaskStatus.PENDING)
                                        TaskStatus.DONE
                                    else
                                        TaskStatus.PENDING
                                )
                            } else current
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit
) {
    val displayText =
        if (task.status == TaskStatus.DONE)
            "✔ ${task.title}"
        else
            task.title

    Text(
        text = displayText,
        modifier = Modifier.clickable { onToggle() }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HoraTheme {
        HomeScreen()
    }
}