package com.ptit.bookapi.models

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BookToPost(
    @SerializedName("BookAuthors")
    val bookAuthors: List<Int>,
    @SerializedName("BookImages")
    val bookImages: List<String>,
    @SerializedName("CategoryID")
    val categoryID: Int,
    @SerializedName("Cost")
    val cost: BigDecimal,
    @SerializedName("Name")
    val name: String,
    @SerializedName("PubDate")
    val pubDate: String,
    @SerializedName("PublisherID")
    val publisherID: Int,
    @SerializedName("Retail")
    val retail: BigDecimal
)