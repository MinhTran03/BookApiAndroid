package com.ptit.bookapi.models


import com.google.gson.annotations.SerializedName

data class BookCategory(
    @SerializedName("Alias")
    val alias: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("Name")
    val name: String
) {
    override fun toString(): String {
        return name
    }
}