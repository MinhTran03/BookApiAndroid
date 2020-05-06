package com.ptit.bookapi.utils

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

    fun bookDeleteAsync(bookId: Int): Boolean = runBlocking {
        apiService.bookDeleteAsync(bookId).await().isSuccessful
    }

    fun authorDeleteAsync(authorId: Int): Boolean = runBlocking {
        apiService.authorDeleteAsync(authorId).await().isSuccessful
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

    fun bookPost(book: BookToPost): Boolean = runBlocking {
        apiService.bookPostAsync(book).await().isSuccessful
    }

    fun bookPut(book: BookToPut): Boolean = runBlocking {
        apiService.bookPutAsync(book).await().isSuccessful
    }

    fun authorPost(author: Author): Boolean = runBlocking {
        apiService.authorPostAsync(author).await().isSuccessful
    }

    fun authorPut(author: Author): Boolean = runBlocking {
        apiService.authorPutAsync(author).await().isSuccessful
    }
}