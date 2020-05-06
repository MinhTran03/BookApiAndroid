package com.ptit.bookapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import com.ptit.bookapi.models.Author
import com.ptit.bookapi.utils.*
import kotlinx.android.synthetic.main.activity_author.*
import kotlinx.android.synthetic.main.content_author.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthorActivity : AppCompatActivity() {

    private var authorId = NOT_A_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author)
        setSupportActionBar(toolbarAuthorDetail)

        authorId = intent.getIntExtra(AUTHOR_ID, NOT_A_ID)

        if(authorId != NOT_A_ID)
            displayAuthor()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item_detail, menu)
        return true
    }

    private fun displayAuthor() {
        val authorResponse = ApiImpl.authorDetailAsync(authorId)

        if(authorResponse != null) {
            editTextAuthorName.setText(authorResponse.fullName)
            editTextAuthorAddress.setText(authorResponse.address)

        }else {
            Snackbar.make(findViewById(android.R.id.content), "Tác giả bạn tìm không tồn tại",
                Snackbar.LENGTH_LONG).show()
            GlobalScope.launch {
                delay(1500)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_save -> {
                val customResponse: CustomResponse? = uploadAuthor()

                if(customResponse?.success == true){
                    Snackbar.make(findViewById(android.R.id.content),
                        "Đã lưu thông tin tác giả", Snackbar.LENGTH_LONG).show()
                }else if(customResponse?.success == false){
                    Helper.showCustomResponseError(this, customResponse)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun uploadAuthor(): CustomResponse? {
        if(!validateForm()) return null

        val author = Author(
            editTextAuthorAddress.text.toString(),
            editTextAuthorName.text.toString(),
            authorId
        )

        if(authorId != NOT_A_ID)
            return ApiImpl.authorPut(author)
        return ApiImpl.authorPost(author)
    }

    private fun validateForm(): Boolean {
        var result = true

        result = validateBaseEditText(editTextAuthorName, R.string.empty_author_name)
        result = validateBaseEditText(editTextAuthorAddress, R.string.empty_author_address)

        return result
    }

    private fun validateBaseEditText(editText: EditText, messageResource: Int): Boolean {
        if(editText.text.isEmpty() || editText.text.isBlank()){
            editText.error = resources.getString(messageResource)
            return false
        }
        return true
    }
}
