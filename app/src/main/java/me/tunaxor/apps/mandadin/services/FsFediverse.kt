package me.tunaxor.apps.mandadin.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import me.tunaxor.apps.mandadin.types.FsFedNote

interface FsFediverseService {
    suspend fun find(page: Int, limit: Int): List<FsFedNote>

    suspend fun findOne(note: String): FsFedNote?
}

class FsFediverseImpl(private val http: HttpClient, private val baseUrl: String) : FsFediverseService {
    override suspend fun find(page: Int, limit: Int): List<FsFedNote> {
        val response = http.get(baseUrl) {
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
        val response = http.get(baseUrl) {
            parameter("note", note)
        }
        return if (response.status.value < 300) {
            response.body()
        } else {
            null
        }
    }

}