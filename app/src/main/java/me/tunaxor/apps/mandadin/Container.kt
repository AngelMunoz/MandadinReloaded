package me.tunaxor.apps.mandadin

import me.tunaxor.apps.mandadin.services.*

interface ITodos {
    val iTodos: ITodoService
}

class AppEnv : ITodos {
    override val iTodos: ITodoService
        get() = TodoService()

}