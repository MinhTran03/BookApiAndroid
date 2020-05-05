package com.ptit.bookapi

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ptit.bookapi.models.Book
import com.ptit.bookapi.utils.ApiImpl
import com.ptit.bookapi.utils.BOOK_ID
import com.ptit.bookapi.utils.Helper
import com.ptit.bookapi.utils.NOT_A_ID

class BookRecyclerAdapter(private val context: Context, private val books: MutableList<Book>) :
    RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>() {

    private val TAG = "BookRecyclerAdapter"
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_book_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = books[position]
        Log.d(TAG, "book id: ${book.iD} - book position: $position")

        holder.bookName.text = book.name
        holder.bookRetail.text =
            Helper.formatCostVnd(book.retail)
        Helper.loadImageToView(
            context,
            holder.bookImageView,
            book.bookImages[0].imagePath
        )

        holder.buttonDeleteBook.setOnClickListener { v: View ->
            if(ApiImpl.bookDeleteAsync(book.iD)) {
                books.removeAll { b -> b.iD == book.iD }

                Snackbar.make(v, "Xóa thành công sách [${book.name}]",
                    Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(v, "Sách [${book.name}] không tồn tại",
                    Snackbar.LENGTH_LONG).show()
            }
            notifyDataSetChanged()
        }

        holder.bookId = book.iD
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById(R.id.textViewBookName)
        val bookRetail: TextView = itemView.findViewById(R.id.textViewBookRetail)
        val bookImageView: ImageView = itemView.findViewById(R.id.imageViewBook)
        val buttonDeleteBook: ImageButton = itemView.findViewById(R.id.imageButtonDelteBook)

        var bookId = NOT_A_ID

        init{
            itemView.setOnClickListener{
                val intent = Intent(context, BookActivity::class.java)
                intent.putExtra(BOOK_ID, bookId)
                context.startActivity(intent)
            }
        }
    }
}