package me.tunaxor.apps.mandadin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.tunaxor.apps.mandadin.types.Todo
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.TodoVm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val env = AppEnv()
        setContent {
            MandadinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    TodoPage(env = env)
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
fun TodoForm(selected: Todo?, onSubmit: (Triple<String, String, Boolean>) -> Unit) {
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
        }
    }
}

@Composable
fun TodoList(todos: List<Todo>?, onSelectionChanged: (Todo?) -> Unit) {
    Column {
        todos?.forEach { todo ->
            Row(modifier = Modifier.clickable {
                onSelectionChanged(todo)
            }) {
                Surface(modifier = Modifier.padding(8.dp)) {
                    Text(text = todo.title)
                }
            }
        }
    }
//    LazyColumn {
//        items(todos ?: emptyList(), key = { it.id }) { todo ->
//            Row(modifier = Modifier.clickable {
//                onSelectionChanged(todo)
//            }) {
//                Surface(modifier = Modifier.padding(8.dp)) {
//                    Text(text = todo.title)
//                }
//            }
//        }
//    }
}

@Composable
fun TodoPage(env: AppEnv) {
    val scope = rememberCoroutineScope()
    val vm = TodoVm(env.iTodos)
    val todos = vm.todos
    var selected by remember { mutableStateOf<Todo?>(null) }

    LaunchedEffect(true) {
        vm.loadTodos()
    }
    Column {
        TodoForm(selected = selected) {
            scope.launch {
                vm.saveTodo(it, selected)
                vm.loadTodos()
            }
        }
        Column {
            todos.forEach { todo ->
                Row(modifier = Modifier.clickable {
                    selected = todo
                }) {
                    Surface(modifier = Modifier.padding(8.dp)) {
                        Text(text = todo.title)
                    }
                }
            }
        }
        TodoList(todos) { selected = it }
    }
}
