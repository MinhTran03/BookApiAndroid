package com.ptit.bookapi

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ptit.bookapi.models.Author
import com.ptit.bookapi.models.Book
import com.ptit.bookapi.utils.ApiImpl
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
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
    private var listAuthor: MutableList<Author> = ApiImpl.authorListAsync()
    private val authorRecyclerAdapter by lazy {
        AuthorRecyclerAdapter(this, listAuthor)
    }
    private val authorLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbarMain)

        fabMain.setOnClickListener {
            if(nav_view.menu.findItem(R.id.nav_book_list).isChecked) {
                val intent = Intent(this, BookActivity::class.java)
                startActivity(intent)
            }else {
            }
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_book_list, R.id.nav_author_list
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)

        displayBooks()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_book_list -> {
                displayBooks()
            }
            R.id.nav_author_list -> {
                displayAuthors()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_reload -> {
                reloadData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun displayBooks() {
        Log.d(TAG, "displayBooks call")
        listItems.layoutManager = bookLayoutManager
        listItems.adapter = bookRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_book_list).isChecked = true
    }

    private fun displayAuthors() {
        Log.d(TAG, "displayAuthors call")
        listItems.layoutManager = authorLayoutManager
        listItems.adapter = authorRecyclerAdapter

        nav_view.menu.findItem(R.id.nav_author_list).isChecked = true
    }

    private fun updateListData(){
        if(nav_view.menu.findItem(R.id.nav_book_list).isChecked) {
            listBook.clear()
            listBook.addAll(ApiImpl.bookListAsync())
        }else{
            listAuthor.clear()
            listAuthor.addAll(ApiImpl.authorListAsync())
        }
    }

    private fun reloadData(){
        Log.d(TAG, "reloadData call")

        updateListData()

        listItems.adapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        updateListData()

        listItems.adapter?.notifyDataSetChanged()
    }
}
