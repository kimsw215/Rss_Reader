package com.example.coroutine_chap2.model

data class Feed(
    val name: String,
    val url: String
)

data class Article(
    val feed: String,
    val title: String,
    val summary: String
)

data class Profile(
    val id: Long,
    val name: String,
    val age: Int
)