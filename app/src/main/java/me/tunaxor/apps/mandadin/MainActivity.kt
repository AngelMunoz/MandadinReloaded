package me.tunaxor.apps.mandadin

import kotlinx.coroutines.launch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.tunaxor.apps.mandadin.types.Todo
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.TodoVm


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val env = AppEnv()
        val todoVm = TodoVm(env.iTodos)
        setContent {
            MandadinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TodoPage(todoVm)
                }
            }
        }
    }
}


@Composable
fun FormLabel(title: String) {
    Text(title, Modifier.padding(2.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForm(
    selected: Todo?,
    onCancel: () -> Unit = {},
    onSubmit: (Triple<String, String, Boolean>) -> Unit = {}
) {
    var title by remember(selected) { mutableStateOf(selected?.title ?: "") }
    var description by remember(selected) { mutableStateOf(selected?.description ?: "") }
    var isDone by remember(selected) { mutableStateOf(selected?.isDone ?: false) }

    Column(modifier = Modifier.padding(8.dp)) {
        Row(modifier = Modifier.padding(4.dp)) {
            TextField(value = title, onValueChange = {
                title = it
            }, placeholder = { Text("Todo Title") }, label = { FormLabel(title = "Todo Title") })
        }
        Row(modifier = Modifier.padding(4.dp)) {
            TextField(value = description,
                onValueChange = {
                    description = it
                },
                placeholder = { Text("Todo Description") },
                label = { FormLabel(title = "Todo Description") })
        }
        Row(modifier = Modifier.padding(4.dp)) {
            FormLabel(title = "Is Done")
            Switch(checked = isDone, onCheckedChange = { isDone = it })
        }
        Row(modifier = Modifier.padding(4.dp)) {
            Button(onClick = {
                onSubmit(Triple(title, description, isDone))
            }) {
                Text("Save Todo")
            }

            Button(onClick = {
                onCancel()
            }) {
                Text("Cancel")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(todo: Todo, modifier: Modifier = Modifier) {
    Surface {
        Box(modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .requiredHeight(50.dp)) {
            Text(text = todo.title)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoList(todos: List<Todo>, onSelectionChanged: (Todo?) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        items(todos, key = { it.id }) { todo ->
            Row(modifier = Modifier
                .animateItemPlacement()
                .clickable {
                    onSelectionChanged(todo)
                }) {
                TodoItem(todo = todo, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPage(todoVm: TodoVm) {
    val scope = rememberCoroutineScope()
    var showForm by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        todoVm.loadTodos()
    }
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            todoVm.selectTodo(null)
            showForm = true
        }) {
            Text("Add Todo")
        }
    }, content = {
        Column(modifier = Modifier.padding(it)) {
            if (showForm) {
                TodoForm(selected = todoVm.selected, onCancel = {
                    showForm = false
                    todoVm.selectTodo(null)
                }) {
                    scope.launch {
                        todoVm.saveTodo(it, todoVm.selected)
                        todoVm.loadTodos()
                    }
                }
            }
            TodoList(todoVm.todos) { todo ->
                todoVm.selectTodo(todo)
                showForm = true
            }
        }

    })
}
