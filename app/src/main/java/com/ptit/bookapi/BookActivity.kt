package com.ptit.bookapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListPopupWindow
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.ptit.bookapi.models.BookToPost
import com.ptit.bookapi.models.BookToPut
import com.ptit.bookapi.utils.ApiImpl
import com.ptit.bookapi.utils.BOOK_ID
import com.ptit.bookapi.utils.Helper
import com.ptit.bookapi.utils.NOT_A_ID
import kotlinx.android.synthetic.main.activity_book.*
import kotlinx.android.synthetic.main.content_book.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG by lazy {
        this.localClassName
    }
    private var bookId = NOT_A_ID
    private var selectedCategoryId = NOT_A_ID
    private var selectedAuthorId = NOT_A_ID
    private var selectedPublisherId = NOT_A_ID

    private val categoryPopupWindow by lazy {
        ListPopupWindow(this)
    }
    private val authorPopupWindow by lazy {
        ListPopupWindow(this)
    }
    private val publisherPopupWindow by lazy {
        ListPopupWindow(this)
    }

    private val bookCategories = ApiImpl.bookCategoryListAsync()
    private val authors = ApiImpl.authorListAsync()
    private val publishers = ApiImpl.publisherListAsync()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        setSupportActionBar(toolbar)

        setEvents()
        setupPopupWindows()

        bookId = intent.getIntExtra(BOOK_ID, NOT_A_ID)

        if(bookId != NOT_A_ID)
            displayBook()
    }

    @SuppressLint("SetTextI18n")
    private fun displayBook() {
        val bookResponse = ApiImpl.bookDetailAsync(bookId)

        if(bookResponse != null) {
            editTextImageUrl.setText(bookResponse.bookImages[0].imagePath)
            editTextBookName.setText(bookResponse.name)
            editTextBookCost.setText(bookResponse.cost.toString())
            editTextBookRetail.setText(bookResponse.retail.toString())
            textViewDate.text = Helper.formatDate(bookResponse.pubDate)

            selectedAuthorId = bookResponse.bookAuthors[0].authorID
            selectedCategoryId = bookResponse.categoryID
            selectedPublisherId = bookResponse.publisherID
            editTextBookCategory.setText(bookCategories.first { b -> b.iD == selectedCategoryId }.name)
            editTextBookAuthor.setText(authors.first { b -> b.iD == selectedAuthorId }.fullName)
            editTextBookPublisher.setText(publishers.first { b -> b.iD == selectedPublisherId }.name)

            Helper.loadImageToView(
                this@BookActivity, imageViewBook,
                bookResponse.bookImages[0].imagePath
            )
        }else {
            Snackbar.make(findViewById(android.R.id.content), "Sách bạn tìm không tồn tại",
                Snackbar.LENGTH_LONG).show()
            GlobalScope.launch {
                delay(1500)
                finish()
            }
        }
    }

    private fun setupPopupWindows(){
        val categoryAdapter = ArrayAdapter(this, R.layout.item_simple_popup,
            R.id.textViewElement, bookCategories)
        categoryPopupWindow.anchorView = editTextBookCategory
        categoryPopupWindow.setAdapter(categoryAdapter)
        categoryPopupWindow.setOnItemClickListener { _, _, position, _ ->
            editTextBookCategory.setText(bookCategories[position].name)
            selectedCategoryId = bookCategories[position].iD
            categoryPopupWindow.dismiss()
        }

        val authorAdapter = ArrayAdapter(this, R.layout.item_simple_popup,
            R.id.textViewElement, authors)
        authorPopupWindow.anchorView = editTextBookAuthor
        authorPopupWindow.setAdapter(authorAdapter)
        authorPopupWindow.setOnItemClickListener { _, _, position, _ ->
            editTextBookAuthor.setText(authors[position].fullName)
            selectedAuthorId = authors[position].iD
            authorPopupWindow.dismiss()
        }

        val publisherAdapter = ArrayAdapter(this, R.layout.item_simple_popup,
            R.id.textViewElement, publishers)
        publisherPopupWindow.anchorView = editTextBookPublisher
        publisherPopupWindow.setAdapter(publisherAdapter)
        publisherPopupWindow.setOnItemClickListener { _, _, position, _ ->
            editTextBookPublisher.setText(publishers[position].name)
            selectedPublisherId = publishers[position].iD
            publisherPopupWindow.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_book, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save_book -> {
                val responseState: Boolean? = if(bookId != NOT_A_ID){
                    Log.d(TAG, "Update book ${editTextBookName.text}")
                    uploadEditedBook()
                }else{
                    Log.d(TAG, "Create new book ${editTextBookName.text}")
                    uploadNewBook()
                }

                if(responseState == true){
                    Snackbar.make(findViewById(android.R.id.content),
                        "Đã lưu thông tin sách", Snackbar.LENGTH_LONG).show()
                }else{
                    Snackbar.make(findViewById(android.R.id.content),
                        "Lỗi lưu sách", Snackbar.LENGTH_LONG).show()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun uploadNewBook(): Boolean? {
        if(!validateForm()) return null

        val bookToUpload = BookToPost(
            listOf(selectedAuthorId),
            listOf(editTextImageUrl.text.toString()),
            selectedCategoryId,
            editTextBookCost.text.toString().toBigDecimal(),
            editTextBookName.text.toString(),
            Helper.formatStringToDateTimeForPost(textViewDate.text.toString()),
            selectedPublisherId,
            editTextBookRetail.text.toString().toBigDecimal()
        )

        return ApiImpl.bookPost(bookToUpload)
    }

    private fun uploadEditedBook(): Boolean? {
        if(!validateForm()) return null

        val bookToUpload = BookToPut(
            bookId,
            listOf(selectedAuthorId),
            listOf(editTextImageUrl.text.toString()),
            selectedCategoryId,
            editTextBookCost.text.toString().toBigDecimal(),
            editTextBookName.text.toString(),
            Helper.formatStringToDateTimeForPost(textViewDate.text.toString()),
            selectedPublisherId,
            editTextBookRetail.text.toString().toBigDecimal()
        )

        return ApiImpl.bookPut(bookToUpload)
    }

    private fun setEvents(){
        buttonSelectDate.setOnClickListener(this)
        buttonChangeImage.setOnClickListener(this)

        editTextBookCategory.setOnClickListener(this)
        editTextBookAuthor.setOnClickListener(this)
        editTextBookPublisher.setOnClickListener(this)
    }

    private fun validateForm(): Boolean {
        var result = true

        result = validateBaseEditText(editTextImageUrl, R.string.empty_book_url_message)
        result = validateBaseEditText(editTextBookName, R.string.empty_book_name_message)
        result = validateBaseEditText(editTextBookCost, R.string.empty_book_cost_message)
        result = validateBaseEditText(editTextBookRetail, R.string.empty_book_retail_message)

        result = validateBaseEditText(editTextBookCategory, R.string.empty_book_category_message)
        result = validateBaseEditText(editTextBookAuthor, R.string.empty_book_author_message)
        result = validateBaseEditText(editTextBookPublisher, R.string.empty_book_publisher_message)

        return result
    }

    private fun validateBaseEditText(editText: EditText, messageResource: Int): Boolean {
        if(editText.text.isEmpty() || editText.text.isBlank()){
            editText.error = resources.getString(messageResource)
            return false
        }
        return true
    }

    override fun onClick(v: View?) {
        when(v?.id){
            buttonSelectDate.id -> {
                Helper.showDatePicker(this, textViewDate)
            }
            buttonChangeImage.id -> {
                Log.d(TAG, "Load image ${editTextImageUrl.text}")
                Helper.loadImageToView(this, imageViewBook, editTextImageUrl.text.toString())
            }
            editTextBookCategory.id -> {
                categoryPopupWindow.show()
            }
            editTextBookAuthor.id -> {
                authorPopupWindow.show()
            }
            editTextBookPublisher.id -> {
                publisherPopupWindow.show()
            }
        }
    }
}
