package me.tunaxor.apps.mandadin

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import me.tunaxor.apps.mandadin.services.*

interface ITodos {
    val Todos: TodoService
}

interface IFsFediverse {
    val FsFediverse: FsFediverseService
}

interface ApplicationEnv: ITodos, IFsFediverse { }
class AppEnv(private val baseUrl: String) : ApplicationEnv {

    private val client  = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    override val Todos: TodoService
        get() = TodoImpl()
    override val FsFediverse: FsFediverseService
        get() = FsFediverseImpl(client, baseUrl = baseUrl)

}