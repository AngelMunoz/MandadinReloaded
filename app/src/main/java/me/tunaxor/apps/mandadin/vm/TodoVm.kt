package me.tunaxor.apps.mandadin.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.tunaxor.apps.mandadin.services.ITodoService
import me.tunaxor.apps.mandadin.types.Todo

class TodoVm(private val service: ITodoService) : ViewModel() {
    var todos: List<Todo> by mutableStateOf(emptyList())

    suspend fun loadTodos() {
        todos = service.find()
    }

    suspend fun saveTodo(newValues: Triple<String, String, Boolean>, existing: Todo?): Todo? {
        val (title, description, isDone) = newValues
        val didSave =
            if (existing !== null) {
                service.update(
                    existing.copy(
                        title = title,
                        description = description,
                        isDone = isDone
                    )
                )
            } else {
                service.create(title, description, isDone)
            }
        return didSave
    }
}