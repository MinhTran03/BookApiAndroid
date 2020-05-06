package com.ptit.bookapi.utils

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ptit.bookapi.models.*
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL = "https://bookapiptit.azurewebsites.net/api/"

const val GET_BOOK_LIST: String = "books/getall"
const val GET_BOOK_DETAIL: String = "books/getbyid/{book_id}"
const val POST_BOOK: String = "books/create"
const val PUT_BOOK: String = "books/update"
const val DELETE_BOOK: String = "books/delete/{book_id}"

const val GET_CATEGORY_LIST: String = "bookcategories/getall"

const val GET_AUTHOR_LIST: String = "authors/getall"
const val GET_AUTHOR_DETAIL: String = "authors/getbyid/{author_id}"
const val POST_AUTHOR: String = "authors/create"
const val PUT_AUTHOR: String = "authors/update"
const val DELETE_AUTHOR: String = "authors/delete/{author_id}"

const val GET_PUBLISHER_LIST: String = "publishers/getall"

interface ApiService {

    @GET(GET_BOOK_DETAIL)
    fun bookDetailAsync(
        @Path("book_id") bookId: Int
    ): Deferred<Response<Book>>

    @GET(GET_AUTHOR_DETAIL)
    fun authorDetailAsync(
        @Path("author_id") authorId: Int
    ): Deferred<Response<Author>>

    @GET(GET_BOOK_LIST)
    fun bookListAsync(): Deferred<MutableList<Book>>

    @DELETE(DELETE_BOOK)
    fun bookDeleteAsync(
        @Path("book_id") bookId: Int
    ): Deferred<Response<ResponseBody>>

    @DELETE(DELETE_AUTHOR)
    fun authorDeleteAsync(
        @Path("author_id") authorId: Int
    ): Deferred<Response<ResponseBody>>

    @GET(GET_CATEGORY_LIST)
    fun bookCategoryListAsync(): Deferred<List<BookCategory>>

    @GET(GET_AUTHOR_LIST)
    fun authorListAsync(): Deferred<MutableList<Author>>

    @GET(GET_PUBLISHER_LIST)
    fun publisherListAsync(): Deferred<List<Publisher>>

    @POST(POST_BOOK)
    fun bookPostAsync(@Body book: BookToPost): Deferred<Response<ResponseBody>>

    @POST(POST_AUTHOR)
    fun authorPostAsync(@Body author: Author): Deferred<Response<ResponseBody>>

    @PUT(PUT_BOOK)
    fun bookPutAsync(@Body book: BookToPut): Deferred<Response<ResponseBody>>

    @PUT(PUT_AUTHOR)
    fun authorPutAsync(@Body author: Author): Deferred<Response<ResponseBody>>

    companion object{
        operator fun invoke(): ApiService {

            val gsonFormat = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gsonFormat))
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}