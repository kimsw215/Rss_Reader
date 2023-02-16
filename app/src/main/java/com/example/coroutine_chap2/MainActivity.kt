package com.example.coroutine_chap2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutine_chap2.adapter.ArticleAdapter
import com.example.coroutine_chap2.model.Article
import com.example.coroutine_chap2.model.Feed
import kotlinx.coroutines.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import javax.xml.parsers.DocumentBuilderFactory

private val dispatcher = newFixedThreadPoolContext(2, "IO")

class MainActivity : AppCompatActivity() {
    private lateinit var articles: RecyclerView
    private lateinit var viewAdapter: ArticleAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val dispatch = newSingleThreadContext(name = "ServiceCall")
    private val factory = DocumentBuilderFactory.newInstance()
    private val feeds = listOf(
        Feed("npr","https://www.npr.org/rss/rss.php?id=1001"),
        Feed("fox","https://feeds.foxnews.com/foxnews/politics?format=xml"),
        //"https://rss.cnn.com/rss/cnn_topstories.rss",  //<- 사이트 변경으로 인하여 오류 발생때문에 주석처리
        Feed("inv","htt://myNewsFeed")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ArticleAdapter()
        articles = findViewById<RecyclerView>(R.id.articles).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        asyncLoadNews()
    }

    private fun asyncLoadNews() = GlobalScope.launch {
        val requests = mutableListOf<Deferred<List<Article>>>()

        // 피드별로 가져온 요소를  피드 목록에 추가한다.
        feeds.mapTo(requests) {
            asyncFetchArticle(it, dispatch)
        }

        // 각 코드가 완료될 때까지 대기하는 코드를 추가한다.
        requests.forEach {
            it.join()
        }

        val article = requests
            .filter { !it.isCancelled }
            .flatMap { it.getCompleted() }

        val failedCount = requests
            .filter { it.isCancelled }
            .size

        val obtained = requests.size - failedCount

        launch(Dispatchers.Main) {
            findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
            viewAdapter.add(article)
       }
    }

    private fun asyncFetchArticle(feed: Feed, dispatcher: CoroutineDispatcher) =
        GlobalScope.async(dispatcher) {
            delay(1000)
            val builder = factory.newDocumentBuilder()
            val xml = builder.parse(feed.url)
            val news = xml.getElementsByTagName("channel").item(0)

            (0 until news.childNodes.length)
                .map { news.childNodes.item(it) }
                .filter { Node.ELEMENT_NODE == it.nodeType }
                .map { it as Element }
                .filter { "item" == it.tagName }
                .map {
                    val title = it.getElementsByTagName("title")
                        .item(0)
                        .textContent
                    var summary = it.getElementsByTagName("description")
                        .item(0)
                        .textContent
                    if(!summary.startsWith("<div") && summary.contains("<div")) {
                        summary = summary.substring(0, summary.indexOf("<div"))
                    }
                    Article(feed.name, title, summary)
                }
        }
}

