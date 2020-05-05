package com.ptit.bookapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ptit.bookapi.models.Book
import com.ptit.bookapi.utils.ApiImpl

import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.content_book_list.*

class BookListActivity : AppCompatActivity() {

    private val TAG by lazy {
        this.localClassName
    }
    private var listBook: MutableList<Book> = ApiImpl.bookListAsync()
    private val bookRecyclerAdapter by lazy {
        Log.d(TAG, "onCreate-BookRecyclerViewAdapter")
        BookRecyclerAdapter(
            this,
            listBook
        )
    }
    private val bookLayoutManager by lazy {
        StaggeredGridLayoutManager(resources.getInteger(R.integer.book_grid_span), LinearLayoutManager.VERTICAL)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, BookActivity::class.java)
            startActivity(intent)
        }

        displayBooks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list_book, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reload -> {
                reloadBooks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayBooks() {
        Log.d(TAG, "displayBooks call")
        listItems.layoutManager = bookLayoutManager
        listItems.adapter = bookRecyclerAdapter
    }

    private fun updateListBook(){
        listBook.clear()
        listBook.addAll(ApiImpl.bookListAsync())
    }

    private fun reloadBooks(){
        Log.d(TAG, "reloadBooks call")

        updateListBook()

        listItems.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        updateListBook()

        listItems.adapter?.notifyDataSetChanged()
    }
}
