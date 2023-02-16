package com.example.coroutine_chap2

import com.example.coroutine_chap2.model.Profile
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.system.measureTimeMillis

fun main(args: Array<String>)  {
    runBlocking {
        val dispatcher = newSingleThreadContext("myDispatcher")
        val name = withContext(dispatcher) {
            // 중요한 작업 수행
            "Susan Calvin"
        }
        println("User: $name")
    }

    /*    val client: ProfileServiceRepository = ProfileServiceClient()
    val profile = client.asyncfetchById(12)
    println(profile)*/

}
interface ProfileServiceRepository {
    suspend fun asyncfetchByName(name: String) : Profile
    suspend fun asyncfetchById(id: Long): Profile
}

class ProfileServiceClient : ProfileServiceRepository {
    override suspend fun asyncfetchByName(name: String) : Profile {
        return Profile(1, name, 28)
    }
    override suspend fun asyncfetchById(id: Long) : Profile {
        return Profile(id,"Susan", 28)
    }
}

