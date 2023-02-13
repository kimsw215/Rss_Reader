package com.example.coroutine_chap2

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import javax.xml.parsers.DocumentBuilderFactory

@OptIn(InternalCoroutinesApi::class)
private val dispatcher = newSingleThreadContext(name = "ServiceCall")
private val factory = DocumentBuilderFactory.newInstance()


fun main(args: Array<String>) = runBlocking {
    val pro = GlobalScope.produce {
        send(5)
        send("a")
    }

    pro.consumeEach {
        println(it)
    }

}


private fun fetchRssHeadlines(): List<String> {
    val builder = factory.newDocumentBuilder()
    val xml = builder.parse("https://www.npr.org/rss/rss.php??id=1001")
    return emptyList()
}
