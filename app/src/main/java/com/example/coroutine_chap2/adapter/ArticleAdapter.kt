package com.example.coroutine_chap2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coroutine_chap2.R
import com.example.coroutine_chap2.model.Article

class ArticleAdapter : RecyclerView.Adapter<ArticleAdapter.ViewHolder>(){
    val articles: MutableList<Article> = mutableListOf()

    class ViewHolder(
        val layout: LinearLayout,
        val feed: TextView,
        val title: TextView,
        val summary: TextView
    ): RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.article, parent, false) as LinearLayout

        val feed = layout.findViewById<TextView>(R.id.feed)
        val title = layout.findViewById<TextView>(R.id.title)
        val summary = layout.findViewById<TextView>(R.id.summary)

        return ViewHolder(layout, feed, title, summary)
    }

    override fun getItemCount() = articles.size

    fun add(article: List<Article>){
        this.articles.addAll(article)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = articles[position]

        holder.feed.text = article.feed
        holder.title.text = article.title
        holder.summary.text = article.summary
    }
}