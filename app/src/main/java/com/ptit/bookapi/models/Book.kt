package com.ptit.bookapi.models

import com.google.gson.annotations.SerializedName
import com.ptit.bookapi.utils.Helper
import java.math.BigDecimal
import java.util.*

data class Book(
    @SerializedName("BookAuthors")
    val bookAuthors: List<BookAuthor>,
    @SerializedName("BookImages")//
    val bookImages: List<BookImage>,
    @SerializedName("CategoryID")//
    val categoryID: Int,
    @SerializedName("Cost")//
    val cost: BigDecimal,
    @SerializedName("ID")//
    val iD: Int,
    @SerializedName("Name")//
    val name: String,
    @SerializedName("PubDate")//
    val pubDate: Date,
    @SerializedName("PublisherID")
    val publisherID: Int,
    @SerializedName("Retail")//
    val retail: BigDecimal
){
    override fun toString(): String {
        return Helper.formatDate(pubDate)
    }
}