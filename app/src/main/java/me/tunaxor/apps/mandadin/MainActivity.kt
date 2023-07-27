package me.tunaxor.apps.mandadin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.tunaxor.apps.mandadin.ui.theme.MandadinTheme
import me.tunaxor.apps.mandadin.vm.TodoVm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MandadinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val env = remember { AppEnv() }
                    TodoPage(env = env)
                }
            }
        }
    }
}

@Composable
fun TodoList(vm: TodoVm) {
    val todos = vm.todos.observeAsState()
    LazyColumn {
        items(todos.value?.size ?: 0) {
            Text(text = todos.value?.get(it)?.title ?: "No title")
        }
    }
}

@Composable
fun TodoPage(env: AppEnv) {
    val vm = rememberUpdatedState(newValue = TodoVm(env.iTodos))

    LaunchedEffect(true) {
        vm.value.loadTodos()
    }
    MandadinTheme {
        TodoList(vm.value)
    }
}