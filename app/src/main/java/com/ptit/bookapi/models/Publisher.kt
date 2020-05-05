package com.ptit.bookapi.models


import com.google.gson.annotations.SerializedName

data class Publisher(
    @SerializedName("Address")
    val address: String,
    @SerializedName("ID")
    val iD: Int,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Phone")
    val phone: String
) {
    override fun toString(): String {
        return name
    }
}