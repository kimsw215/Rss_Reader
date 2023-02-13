package com.example.coroutine_chap2

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        val job = GlobalScope.launch {
            delay(2000)
        }

        // Wait for it to complete once
        job.join()

        // Restart the Job
        job.start()
        job.join()
    }
    println("Took $time ms")
}
