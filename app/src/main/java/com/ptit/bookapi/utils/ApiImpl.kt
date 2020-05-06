package com.ptit.bookapi.utils

import com.google.gson.Gson
import com.ptit.bookapi.models.*
import kotlinx.coroutines.runBlocking

object ApiImpl {
    private val apiService: ApiService = ApiService()

    fun bookListAsync(): MutableList<Book> = runBlocking{
        apiService.bookListAsync().await()
    }

    fun bookDetailAsync(bookId: Int): Book? = runBlocking{
        val deferred = apiService.bookDetailAsync(bookId).await()
        if(deferred.isSuccessful){
            deferred.body()
        }else{
            null
        }
    }

    fun authorDetailAsync(authorId: Int): Author? = runBlocking{
        val deferred = apiService.authorDetailAsync(authorId).await()
        if(deferred.isSuccessful){
            deferred.body()
        }else{
            null
        }
    }

    fun bookDeleteAsync(bookId: Int): CustomResponse = runBlocking {
        val response = apiService.bookDeleteAsync(bookId).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())

        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }

    fun authorDeleteAsync(authorId: Int): CustomResponse = runBlocking {
        val response = apiService.authorDeleteAsync(authorId).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())

        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }

    fun bookCategoryListAsync(): List<BookCategory> = runBlocking {
        apiService.bookCategoryListAsync().await()
    }

    fun authorListAsync(): MutableList<Author> = runBlocking {
        apiService.authorListAsync().await()
    }

    fun publisherListAsync(): List<Publisher> = runBlocking {
        apiService.publisherListAsync().await()
    }

    fun bookPost(book: BookToPost): CustomResponse = runBlocking {
        val response = apiService.bookPostAsync(book).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())
        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }

    fun bookPut(book: BookToPut): CustomResponse = runBlocking {
        val response = apiService.bookPutAsync(book).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())
        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }

    fun authorPost(author: Author): CustomResponse = runBlocking {
        val response = apiService.authorPostAsync(author).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())
        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }

    fun authorPut(author: Author): CustomResponse = runBlocking {
        val response = apiService.authorPutAsync(author).await()
        if(response.isSuccessful)
            CustomResponse(response.isSuccessful, response.code(), response.body())
        CustomResponse(response.isSuccessful, response.code(), response.errorBody())
    }
}