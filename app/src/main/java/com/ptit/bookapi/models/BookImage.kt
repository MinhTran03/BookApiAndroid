package com.ptit.bookapi.models


import com.google.gson.annotations.SerializedName

data class BookImage(
    @SerializedName("BookId")
    val bookId: Int,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("ImagePath")
    val imagePath: String
)