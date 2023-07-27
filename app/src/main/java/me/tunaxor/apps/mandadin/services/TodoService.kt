package me.tunaxor.apps.mandadin.services

import kotlinx.coroutines.delay
import me.tunaxor.apps.mandadin.types.Todo

interface ITodoService {
    suspend fun find(): List<Todo>
    suspend fun findOne(id: Int): Todo?
    suspend fun create(title: String, description: String, isDone: Boolean?): Todo?
    suspend fun update(todo: Todo): Todo?
    suspend fun delete(id: Int): Boolean
}

class TodoService : ITodoService {

    private var todos = listOf(
        Todo(1, "title", "description", false, listOf("tag1", "tag2")),
        Todo(2, "title", "description", false, listOf("tag1", "tag2")),
        Todo(3, "title", "description", false, listOf("tag1", "tag2")),
        Todo(4, "title", "description", false, listOf("tag1", "tag2")),
        Todo(5, "title", "description", false, listOf("tag1", "tag2"))
    )

    override suspend fun find(): List<Todo> {
        delay(300)
        return todos
    }

    override suspend fun findOne(id: Int): Todo? {
        delay(300)
        return todos.find { it.id == id }
    }

    override suspend fun create(title: String, description: String, isDone: Boolean?): Todo? {
        val todo =
            Todo(
                todos.size + 1,
                title,
                description,
                false,
                emptyList()
            )
        todos += todo
        delay(300)
        return todo
    }

    override suspend fun update(todo: Todo): Todo? {
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index == -1) return null
        todos = todos.map {
            if (it.id == index) {
                todo
            } else it
        }
        delay(300)
        return todo
    }

    override suspend fun delete(id: Int): Boolean {
        val index = todos.indexOfFirst { it.id == id }
        if (index == -1) return false
        todos = todos.filter { it.id != index }
        delay(300)
        return true
    }
}