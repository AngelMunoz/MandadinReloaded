package me.tunaxor.apps.mandadin.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import me.tunaxor.apps.mandadin.types.FsFedNote

interface IFetchFsFedNotes {
    suspend fun find(page: Int, limit: Int): List<FsFedNote>

    suspend fun findOne(note: String): FsFedNote?
}

class FsFedNotesService(private val http: HttpClient) : IFetchFsFedNotes {
    override suspend fun find(page: Int, limit: Int): List<FsFedNote> {
        val response = http.get("") {
            parameter("page", page)
            parameter("limit", limit)
        }
        return if (response.status.value < 300) {
            response.body()
        } else {
            emptyList()
        }
    }

    override suspend fun findOne(note: String): FsFedNote? {
        val response = http.get("") {
            parameter("note", note)
        }
        return if (response.status.value < 300) {
            response.body()
        } else {
            null
        }
    }

}