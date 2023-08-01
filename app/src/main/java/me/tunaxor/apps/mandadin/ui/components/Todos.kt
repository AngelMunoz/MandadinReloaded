package me.tunaxor.apps.mandadin.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.tunaxor.apps.mandadin.types.Todo
import me.tunaxor.apps.mandadin.vm.TodoVm

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
        Box(
            modifier = modifier
                .padding(4.dp)
                .fillMaxWidth()
                .requiredHeight(50.dp)
        ) {
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
    val botomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = true, SheetValue.Hidden, skipHiddenState = false
        )
    )
    LaunchedEffect(true) {
        todoVm.loadTodos()
    }

    BottomSheetScaffold(sheetContent = {
        TodoForm(selected = todoVm.selected, onCancel = {
            todoVm.selectTodo(null)
            scope.launch {
                botomSheetScaffoldState.bottomSheetState.hide()
            }
        }) {
            scope.launch {
                todoVm.saveTodo(it, todoVm.selected)
                todoVm.loadTodos()
            }
        }
    }) {
        Column(modifier = Modifier.padding(it)) {
            TodoList(todoVm.todos) { todo ->
                todoVm.selectTodo(todo)
                scope.launch {
                    botomSheetScaffoldState.bottomSheetState.show()
                }
            }
        }
    }
}

