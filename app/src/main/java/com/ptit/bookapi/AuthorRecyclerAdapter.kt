package com.ptit.bookapi

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.ptit.bookapi.models.Author
import com.ptit.bookapi.utils.AUTHOR_ID
import com.ptit.bookapi.utils.ApiImpl
import com.ptit.bookapi.utils.Helper
import com.ptit.bookapi.utils.NOT_A_ID

class AuthorRecyclerAdapter(private val context: Context, private val authors: MutableList<Author>) :
    RecyclerView.Adapter<AuthorRecyclerAdapter.ViewHolder>() {

    private val TAG = "AuthorRecyclerAdapter"
    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorRecyclerAdapter.ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.item_author_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = authors.size

    override fun onBindViewHolder(holder: AuthorRecyclerAdapter.ViewHolder, position: Int) {
        val author = authors[position]
        Log.d(TAG, "author id: ${author.iD} - author position: $position")

        holder.authorName.text = author.fullName
        holder.authorAddress.text = author.address
        holder.authorId = author.iD

        holder.buttonDeleteAuthor.setOnClickListener { v: View ->
            val customResponse = ApiImpl.authorDeleteAsync(author.iD)
            if(customResponse.success) {
                authors.removeAll { b -> b.iD == author.iD }

                Snackbar.make(v, "Xóa thành công tác giả [${author.fullName}]",
                    Snackbar.LENGTH_LONG).show()
            }else{
                Helper.showCustomResponseError(context, customResponse)
            }
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val authorName: TextView = itemView.findViewById(R.id.textViewAuthorName)
        val authorAddress: TextView = itemView.findViewById(R.id.textViewAuthorAddress)
        val buttonDeleteAuthor: FloatingActionButton = itemView.findViewById(R.id.fabButtonDeleteAuthor)

        var authorId = NOT_A_ID

        init{
            itemView.setOnClickListener{
                val intent = Intent(context, AuthorActivity::class.java)
                intent.putExtra(AUTHOR_ID, authorId)
                context.startActivity(intent)
            }
        }
    }
}