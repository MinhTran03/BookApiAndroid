package com.ptit.bookapi.models


import com.google.gson.annotations.SerializedName

data class BookAuthor(
    @SerializedName("AuthorID")
    val authorID: Int,
    @SerializedName("BookID")
    val bookID: Int
)