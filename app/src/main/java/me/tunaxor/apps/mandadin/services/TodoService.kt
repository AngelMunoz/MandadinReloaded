package me.tunaxor.apps.mandadin.services

import kotlinx.coroutines.delay
import me.tunaxor.apps.mandadin.types.Todo
import java.util.UUID

interface ITodoService {
    suspend fun find(): List<Todo>
    suspend fun findOne(id: UUID): Todo?
    suspend fun create(title: String, description: String): Todo?
    suspend fun update(todo: Todo): Todo?
    suspend fun delete(id: UUID): Boolean
}

fun generateTodos(amount: Int = 5): List<Todo> {
    val todos = mutableListOf<Todo>()
    for (i in 1..amount) {
        todos.add(
            Todo(
                UUID.randomUUID(), "title - $i", "description - $i", false, emptyList()
            )
        )
    }
    return todos
}

class TodoService : ITodoService {

    private var todos = generateTodos()

    override suspend fun find(): List<Todo> {
        delay(300)
        return todos
    }

    override suspend fun findOne(id: UUID): Todo? {
        delay(300)
        return todos.find { it.id == id }
    }

    override suspend fun create(title: String, description: String): Todo? {
        val todo = Todo(
            UUID.randomUUID(), title, description, false, emptyList()
        )
        todos = todos + todo
        delay(300)
        return todo
    }

    override suspend fun update(todo: Todo): Todo? {
        val found = todos.indexOfFirst { it.id == todo.id }
        if (found == -1) return null
        todos = todos.mapIndexed { index, existing ->
            if (found == index) {
                todo
            } else {
                existing
            }
        }
        delay(300)
        return todo
    }

    override suspend fun delete(id: UUID): Boolean {
        val found = todos.indexOfFirst { it.id == id }
        if (found == -1) return false
        todos = todos.filter { it.id != id }
        delay(300)
        return true
    }
}