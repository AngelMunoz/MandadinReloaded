package me.tunaxor.apps.mandadin.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.tunaxor.apps.mandadin.services.ITodoService
import me.tunaxor.apps.mandadin.types.Todo

class TodoVm(private val service: ITodoService): ViewModel() {
    val todos = MutableLiveData<List<Todo>>()
    suspend fun loadTodos() {
        todos.value = service.find()
    }
}