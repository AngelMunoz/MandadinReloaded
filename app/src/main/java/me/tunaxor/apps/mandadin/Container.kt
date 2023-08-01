package me.tunaxor.apps.mandadin

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import me.tunaxor.apps.mandadin.services.*

interface ITodos {
    val iTodos: ITodoService
}

interface IFsFedNotes {
    val iFsFedNotes: IFetchFsFedNotes
}

interface ApplicationEnv: ITodos, IFsFedNotes { }
class AppEnv : ApplicationEnv {

    private val client  = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    override val iTodos: ITodoService
        get() = TodoService()
    override val iFsFedNotes: IFetchFsFedNotes
        get() = FsFedNotesService(client)

}