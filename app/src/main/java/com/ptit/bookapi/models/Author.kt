package com.ptit.bookapi.models


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("Address")
    val address: String,
    @SerializedName("FullName")
    val fullName: String,
    @SerializedName("ID")
    val iD: Int
) {
    override fun toString(): String {
        return fullName
    }
}