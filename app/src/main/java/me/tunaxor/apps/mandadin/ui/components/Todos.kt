package me.tunaxor.apps.mandadin.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import me.tunaxor.apps.mandadin.types.Todo
import me.tunaxor.apps.mandadin.vm.TodoFormVm
import me.tunaxor.apps.mandadin.vm.TodoPageVm

@Composable
fun TodoForm(
    selected: Todo?,
    modifier: Modifier = Modifier,
    onCancel: () -> Unit = {},
    onSubmit: (TodoFormVm) -> Unit = {}
) {
    val todo = TodoFormVm.from(selected)
    Surface(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(value = todo.title, onValueChange = { title ->
                todo.title = title
            }, placeholder = { Text("Todo Title") }, label = { FormLabel(title = "Todo Title") })
            TextField(value = todo.description,
                onValueChange = { description ->
                    todo.description = description
                },
                placeholder = { Text("Todo Description") },
                label = { FormLabel(title = "Todo Description") })
            Row(modifier = Modifier.padding(4.dp)) {
                FormLabel(title = "Is Done")
                Switch(checked = todo.isDone, onCheckedChange = { todo.isDone = it })
            }
            Row(modifier = Modifier.padding(4.dp)) {
                Button(
                    onClick = {
                        onSubmit(todo)
                    }, modifier = Modifier.padding(4.dp), shape = RoundedCornerShape(4.dp)
                ) {
                    Text("Save Todo")
                }
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.padding(4.dp),
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun TodoItem(todo: Todo, modifier: Modifier = Modifier, onChecked: (Boolean) -> Unit) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth()
            .requiredHeight(50.dp)
    ) {
        val textDecoration = if (todo.isDone) TextDecoration.LineThrough else TextDecoration.None
        Row {
            Text(
                text = todo.title,
                modifier = Modifier
                    .padding(4.dp),
                textDecoration = textDecoration
            )
            Text(
                text = "${todo.description.take(20)}...",
                modifier = Modifier
                    .padding(4.dp),
                textDecoration = textDecoration
            )
            Checkbox(checked = todo.isDone, onCheckedChange = { onChecked(it) }, )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoList(
    todos: List<Todo>,
    modifier: Modifier = Modifier,
    onTodoCheckChanged: (Todo, Boolean) -> Unit,
    onSelectionChanged: (Todo?) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(todos, key = { it.id }) { todo ->
                Row(modifier = Modifier
                    .animateItemPlacement()
                    .hoverable(interactionSource = remember { MutableInteractionSource() })
                    .clickable {
                        onSelectionChanged(todo)
                    }) {
                    TodoItem(
                        todo = todo,
                        modifier = Modifier.fillMaxWidth(),
                        onChecked = { checked ->
                            onTodoCheckChanged(todo, checked)
                        })
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoPage(todoPageVm: TodoPageVm = viewModel()) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false, initialValue = SheetValue.PartiallyExpanded
        )
    )
    LaunchedEffect(true) {
        todoPageVm.loadTodos()
    }

    BottomSheetScaffold(scaffoldState = scaffoldState, sheetContent = {
        TodoForm(selected = todoPageVm.selected, onCancel = {
            todoPageVm.selectTodo(null)
            scope.launch {
                if (scaffoldState.bottomSheetState.isVisible) {
                    scaffoldState.bottomSheetState.hide()
                }
            }
        }) {
            scope.launch {
                todoPageVm.saveTodo(it, todoPageVm.selected)
                todoPageVm.loadTodos()
            }
        }
    }) {
        TodoList(todoPageVm.todos,
            modifier = Modifier.padding(it),
            onTodoCheckChanged = { todo, checked ->
                scope.launch {
                    todoPageVm.updateCheck(todo, checked)
                    todoPageVm.loadTodos()
                }
            }) { todo ->
            todoPageVm.selectTodo(todo)
            scope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        }
    }
}
