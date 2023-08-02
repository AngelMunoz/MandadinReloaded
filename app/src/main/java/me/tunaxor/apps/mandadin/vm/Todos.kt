package me.tunaxor.apps.mandadin.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import me.tunaxor.apps.mandadin.services.TodoService
import me.tunaxor.apps.mandadin.types.Todo

class TodoFormVm : ViewModel() {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var isDone by mutableStateOf(false)

    companion object {
        fun from(todo: Todo?): TodoFormVm {
            val vm = TodoFormVm()
            if (todo !== null) {
                vm.title = todo.title
                vm.description = todo.description
                vm.isDone = todo.isDone
            }
            return vm
        }
    }
}

class TodoPageVm(private val service: TodoService) : ViewModel() {
    var todos: List<Todo> by mutableStateOf(emptyList())
        private set
    var selected: Todo? by mutableStateOf(null)
        private set

    fun selectTodo(todo: Todo?) {
        selected = todo
    }

    suspend fun loadTodos() {
        todos = service.find()
    }

    suspend fun updateCheck(todo: Todo, isDone: Boolean): Todo? {
        return service.update(todo.copy(isDone = isDone))
    }

    suspend fun saveTodo(newValues: TodoFormVm, existing: Todo?): Todo? {
        val title = newValues.title
        val description = newValues.description
        val isDone = newValues.isDone
        val didSave = if (existing !== null) {
            service.update(
                existing.copy(
                    title = title, description = description, isDone = isDone
                )
            )
        } else {
            service.create(title, description)
        }
        return didSave
    }
}

